package com.chefsdelights.farmersrespite.common.block.entity;

import com.chefsdelights.farmersrespite.common.block.KettleBlock;
import com.chefsdelights.farmersrespite.common.block.entity.container.KettleContainer;
import com.chefsdelights.farmersrespite.common.block.entity.inventory.ItemHandler;
import com.chefsdelights.farmersrespite.common.block.entity.inventory.ItemStackHandler;
import com.chefsdelights.farmersrespite.common.block.entity.inventory.KettlePotInventory;
import com.chefsdelights.farmersrespite.common.block.entity.inventory.RecipeWrapper;
import com.chefsdelights.farmersrespite.common.crafting.KettleRecipe;
import com.chefsdelights.farmersrespite.core.FarmersRespite;
import com.chefsdelights.farmersrespite.core.registry.FRBlockEntityTypes;
import com.chefsdelights.farmersrespite.core.registry.FRRecipeSerializers;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import java.util.Map;
import java.util.Optional;

public class KettleBlockEntity extends SyncedBlockEntity implements ExtendedMenuProvider<BlockPos>, HeatableBlockEntity, Nameable, RecipeCraftingHolder {
    private static final Codec<Map<Identifier, Integer>> RECIPES_USED_CODEC = Codec.unboundedMap(Identifier.CODEC, Codec.INT);
    public static final String TAG_KEY_COOK_RECIPES_USED = "RecipesUsed";
    public static final int MEAL_DISPLAY_SLOT = 2;
    public static final int CONTAINER_SLOT = 3;
    public static final int OUTPUT_SLOT = 4;
    public static final int INVENTORY_SIZE = OUTPUT_SLOT + 1;
    private final KettlePotInventory inventory = new KettlePotInventory(this);
    private Component customName;
    private int cookTime;
    private int cookTimeTotal;
    private ItemStack mealContainer;
    protected final ContainerData cookingPotData;
    private final Object2IntOpenHashMap<Identifier> experienceTracker;
    private RecipeHolder<?> recipeUsed;
    private Identifier lastRecipeID;
    private boolean checkNewRecipe;
    private boolean needWater;

    public KettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(FRBlockEntityTypes.KETTLE, blockPos, blockState);
        this.mealContainer = ItemStack.EMPTY;
        this.cookingPotData = new KettleBlockEntity.CookingPotSyncedData();
        this.experienceTracker = new Object2IntOpenHashMap<>();
        this.checkNewRecipe = true;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
        return new KettleContainer(id, player, this, cookingPotData);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.inventory.deserialize(input.childOrEmpty("Inventory"));
        this.cookTime = input.getIntOr("CookTime", 0);
        this.cookTimeTotal = input.getIntOr("CookTimeTotal", 0);
        this.mealContainer = input.read("Container", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        this.needWater = input.getBooleanOr("NeedWater", false);
        this.customName = input.read("CustomName", ComponentSerialization.CODEC).orElse(null);
        this.experienceTracker.clear();
        this.experienceTracker.putAll(input.read(TAG_KEY_COOK_RECIPES_USED, RECIPES_USED_CODEC).orElse(Map.of()));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("CookTime", this.cookTime);
        output.putInt("CookTimeTotal", this.cookTimeTotal);
        output.storeNullable("CustomName", ComponentSerialization.CODEC, this.customName);
        output.putBoolean("NeedWater", needWater);
        output.store("Container", ItemStack.OPTIONAL_CODEC, this.mealContainer);
        this.inventory.serialize(output.child("Inventory"));
        output.store(TAG_KEY_COOK_RECIPES_USED, RECIPES_USED_CODEC, this.experienceTracker);
    }

    public CompoundTag writeMeal(CompoundTag tag, HolderLookup.Provider registries) {
        if (this.getMeal().isEmpty()) {
            return tag;
        }
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        output.storeNullable("CustomName", ComponentSerialization.CODEC, this.customName);
        output.store("Container", ItemStack.OPTIONAL_CODEC, this.mealContainer);
        ItemStackHandler drops = new ItemStackHandler(INVENTORY_SIZE);
        for (int i = 0; i < INVENTORY_SIZE; ++i) {
            drops.setItem(i, i == MEAL_DISPLAY_SLOT ? this.inventory.getItem(i) : ItemStack.EMPTY);
        }
        drops.serialize(output.child("Inventory"));
        CompoundTag result = output.buildResult();
        for (String key : result.keySet()) {
            tag.put(key, result.get(key));
        }
        return tag;
    }

    public Component getName() {
        return this.customName != null ? this.customName : FarmersRespite.i18n("container.kettle");
    }

    public Component getDisplayName() {
        return this.getName();
    }

    public void setCustomName(Component customName) {
        this.customName = customName;
    }

    public static void cookingTick(ServerLevel level, BlockPos pos, BlockState state, KettleBlockEntity cookingPot) {
        boolean isHeated = cookingPot.isHeated(level, pos);
        boolean dirty = false;
        if (isHeated && cookingPot.hasInput()) {
            Optional<RecipeHolder<KettleRecipe>> recipe = cookingPot.getMatchingRecipe(new RecipeWrapper(cookingPot.inventory), level);
            if (recipe.isPresent() && cookingPot.canCook(recipe.get().value())) {
                dirty = cookingPot.processCooking(recipe.get(), level);
            } else {
                cookingPot.cookTime = 0;
            }
        } else if (cookingPot.cookTime > 0) {
            cookingPot.cookTime = Mth.clamp(cookingPot.cookTime - 2, 0, cookingPot.cookTimeTotal);
        }

        ItemStack meal = cookingPot.getMeal();
        if (!meal.isEmpty()) {
            if (!cookingPot.doesMealHaveContainer(meal)) {
                cookingPot.moveMealToOutput();
                dirty = true;
            } else if (!cookingPot.getInventory().getItem(CONTAINER_SLOT).isEmpty()) {
                cookingPot.useStoredContainersOnMeal();
                dirty = true;
            }
        }

        if (dirty) {
            cookingPot.inventoryChanged();
        }
    }

    public @Nullable Component getCustomName() {
        return this.customName;
    }

    private Optional<RecipeHolder<KettleRecipe>> getMatchingRecipe(RecipeWrapper inventory, ServerLevel level) {
        if (this.lastRecipeID != null) {
            Optional<RecipeHolder<?>> known = level.recipeAccess().byKey(ResourceKey.create(Registries.RECIPE, this.lastRecipeID));
            if (known.isPresent() && known.get().value() instanceof KettleRecipe kettleRecipe) {
                if (kettleRecipe.matches(inventory, level)) {
                    @SuppressWarnings("unchecked")
                    RecipeHolder<KettleRecipe> typed = (RecipeHolder<KettleRecipe>) known.get();
                    return Optional.of(typed);
                }
                if (ItemStack.isSameItem(kettleRecipe.result(), this.getMeal())) {
                    return Optional.empty();
                }
            }
        }

        if (this.checkNewRecipe) {
            Optional<RecipeHolder<KettleRecipe>> recipe = level.recipeAccess().getRecipeFor(FRRecipeSerializers.BREWING, inventory, level);
            recipe.ifPresent(holder -> this.lastRecipeID = holder.id().identifier());
            return recipe;
        }

        this.checkNewRecipe = false;
        return Optional.empty();
    }

    public ItemStack getMealContainer() {
        return !this.mealContainer.isEmpty() ? this.mealContainer : remainderOf(this.getMeal());
    }

    private static ItemStack remainderOf(ItemStack stack) {
        ItemStackTemplate remainder = stack.getCraftingRemainder();
        return remainder != null ? remainder.create() : ItemStack.EMPTY;
    }

    private boolean hasInput() {
        for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
            if (!this.inventory.getItem(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    protected boolean canCook(KettleRecipe recipeIn) {
        needWater = recipeIn.getNeedWater();
        if (this.hasInput() && recipeIn != null) {
            ItemStack recipeOutput = recipeIn.result();
            if (recipeOutput.isEmpty()) {
                return false;
            } else {
                ItemStack currentOutput = this.inventory.getItem(MEAL_DISPLAY_SLOT);
                if ((isWater() || !needWater)) {
                    if (currentOutput.isEmpty()) {
                        return true;
                    } else if (!ItemStack.isSameItem(currentOutput, recipeOutput)) {
                        return false;
                    } else if (currentOutput.getCount() + recipeOutput.getCount() <= this.inventory.getMaxCountForSlot(MEAL_DISPLAY_SLOT)) {
                        return true;
                    } else {
                        return currentOutput.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxStackSize();
                    }
                }
            }
        }
        return false;
    }

    private boolean processCooking(RecipeHolder<KettleRecipe> holder, ServerLevel level) {
        KettleRecipe recipe = holder.value();
        if (recipe != null) {
            int j = this.getBlockState().getValue(KettleBlock.WATER_LEVEL);
            ++this.cookTime;
            this.cookTimeTotal = recipe.brewTime;
            if (this.cookTime < this.cookTimeTotal) {
                return false;
            } else {
                this.cookTime = 0;
                this.mealContainer = recipe.getOutputContainer();
                ItemStack recipeOutput = recipe.result();
                ItemStack currentOutput = this.inventory.getItem(MEAL_DISPLAY_SLOT);
                if (currentOutput.isEmpty()) {
                    this.inventory.setItem(MEAL_DISPLAY_SLOT, recipeOutput.copy());
                } else if (currentOutput.getItem() == recipeOutput.getItem()) {
                    currentOutput.grow(recipeOutput.getCount());
                }

                this.trackRecipeExperience(holder);
                if (needWater) {
                    level.setBlockAndUpdate(worldPosition, this.getBlockState().setValue(KettleBlock.WATER_LEVEL, j - 1));
                }

                for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
                    ItemStack itemStack = this.inventory.getItem(i);
                    ItemStackTemplate remainder = itemStack.getCraftingRemainder();
                    if (remainder != null) {
                        Direction direction = this.getBlockState().getValue(KettleBlock.FACING).getCounterClockWise();
                        double dropX = (double) this.worldPosition.getX() + 0.5 + (double) direction.getStepX() * 0.25;
                        double dropY = (double) this.worldPosition.getY() + 0.7;
                        double dropZ = (double) this.worldPosition.getZ() + 0.5 + (double) direction.getStepZ() * 0.25;
                        ItemEntity entity = new ItemEntity(level, dropX, dropY, dropZ, remainder.create());
                        entity.setDeltaMovement((float) direction.getStepX() * 0.08F, 0.25, (float) direction.getStepZ() * 0.08F);
                        level.addFreshEntity(entity);
                    }

                    if (!this.inventory.getItem(i).isEmpty()) {
                        this.inventory.getItem(i).shrink(1);
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public void trackRecipeExperience(@Nullable RecipeHolder<?> holder) {
        if (holder != null) {
            this.experienceTracker.addTo(holder.id().identifier(), 1);
        }
    }

    public void clearUsedRecipes(Player player) {
        if (player.level() instanceof ServerLevel server) {
            this.grantStoredRecipeExperience(server, player.position());
        }
        this.experienceTracker.clear();
    }

    public void grantStoredRecipeExperience(ServerLevel level, Vec3 pos) {
        for (Object2IntMap.Entry<Identifier> entry : this.experienceTracker.object2IntEntrySet()) {
            level.recipeAccess().byKey(ResourceKey.create(Registries.RECIPE, entry.getKey())).ifPresent(holder -> {
                if (holder.value() instanceof KettleRecipe kettle) {
                    splitAndSpawnExperience(level, pos, entry.getIntValue(), kettle.getExperience());
                }
            });
        }
    }

    private static void splitAndSpawnExperience(ServerLevel level, Vec3 pos, int craftedAmount, float experience) {
        int expTotal = Mth.floor((float) craftedAmount * experience);
        float expFraction = Mth.frac((float) craftedAmount * experience);
        if (expFraction != 0.0F && Math.random() < (double) expFraction) {
            ++expTotal;
        }

        while (expTotal > 0) {
            int expValue = ExperienceOrb.getExperienceValue(expTotal);
            expTotal -= expValue;
            level.addFreshEntity(new ExperienceOrb(level, pos.x, pos.y, pos.z, expValue));
        }
    }

    public static void animationTick(Level level, BlockPos pos, BlockState state, KettleBlockEntity cookingPot) {
        if (level != null && cookingPot.isHeated(level, pos)) {
            RandomSource random = level.getRandom();
            double baseX;
            double baseY;
            double baseZ;
            if (random.nextFloat() < 0.2F) {
                baseX = (double) pos.getX() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
                baseY = (double) pos.getY() + 0.7;
                baseZ = (double) pos.getZ() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
                level.addParticle(ParticleTypes.BUBBLE_POP, baseX, baseY, baseZ, 0.0, 0.0, 0.0);
            }

            if (random.nextFloat() < 0.05F) {
                baseX = (double) pos.getX() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                baseY = (double) pos.getY() + 0.5;
                baseZ = (double) pos.getZ() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                double motionY = random.nextBoolean() ? 0.015 : 0.005;
                level.addParticle(ParticleTypes.SMOKE, baseX, baseY, baseZ, 0.0, motionY, 0.0);
            }
        }
    }

    public ItemStack getMeal() {
        return this.inventory.getItem(MEAL_DISPLAY_SLOT);
    }

    public boolean isHeated() {
        return this.level != null && this.isHeated(this.level, this.worldPosition);
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();

        for (int i = 0; i < INVENTORY_SIZE; ++i) {
            drops.add(i == MEAL_DISPLAY_SLOT ? ItemStack.EMPTY : this.inventory.getItem(i));
        }

        return drops;
    }

    private void moveMealToOutput() {
        ItemStack mealDisplay = this.inventory.getItem(MEAL_DISPLAY_SLOT);
        ItemStack finalOutput = this.inventory.getItem(OUTPUT_SLOT);
        int mealCount = Math.min(mealDisplay.getCount(), mealDisplay.getMaxStackSize() - finalOutput.getCount());
        if (finalOutput.isEmpty()) {
            this.inventory.setItem(OUTPUT_SLOT, mealDisplay.split(mealCount));
        } else if (finalOutput.getItem() == mealDisplay.getItem()) {
            mealDisplay.shrink(mealCount);
            finalOutput.grow(mealCount);
        }
    }

    private void useStoredContainersOnMeal() {
        ItemStack mealDisplay = this.inventory.getItem(MEAL_DISPLAY_SLOT);
        ItemStack containerInput = this.inventory.getItem(CONTAINER_SLOT);
        ItemStack finalOutput = this.inventory.getItem(OUTPUT_SLOT);
        if (this.isContainerValid(containerInput) && finalOutput.getCount() < finalOutput.getMaxStackSize()) {
            int smallerStack = Math.min(mealDisplay.getCount(), containerInput.getCount());
            int mealCount = Math.min(smallerStack, mealDisplay.getMaxStackSize() - finalOutput.getCount());
            if (finalOutput.isEmpty()) {
                containerInput.shrink(mealCount);
                this.inventory.setItem(OUTPUT_SLOT, mealDisplay.split(mealCount));
            } else if (finalOutput.getItem() == mealDisplay.getItem()) {
                mealDisplay.shrink(mealCount);
                containerInput.shrink(mealCount);
                finalOutput.grow(mealCount);
            }
        }
    }

    public ItemStack useHeldItemOnMeal(ItemStack container) {
        if (this.isContainerValid(container) && !this.getMeal().isEmpty()) {
            container.shrink(1);
            return this.getMeal().split(1);
        } else {
            return ItemStack.EMPTY;
        }
    }

    private boolean doesMealHaveContainer(ItemStack meal) {
        return !this.mealContainer.isEmpty() || meal.getCraftingRemainder() != null;
    }

    public boolean isContainerValid(ItemStack containerItem) {
        if (containerItem.isEmpty()) {
            return false;
        } else {
            return !this.mealContainer.isEmpty() ? ItemStack.isSameItem(this.mealContainer, containerItem) : ItemStack.isSameItem(remainderOf(this.getMeal()), containerItem);
        }
    }

    public ItemHandler getInventory() {
        return this.inventory;
    }

    public void onInventoryChanged() {
        this.inventoryChanged();
    }

    public void setCheckNewRecipe(boolean checkNewRecipe) {
        this.checkNewRecipe = checkNewRecipe;
    }

    @Override
    public void setRecipeUsed(RecipeHolder<?> holder) {
        this.recipeUsed = holder;
    }

    @Override
    public RecipeHolder<?> getRecipeUsed() {
        return this.recipeUsed;
    }

    private class CookingPotSyncedData implements ContainerData {
        private CookingPotSyncedData() {
        }

        public int get(int index) {
            int var10000;
            switch (index) {
                case 0:
                    var10000 = KettleBlockEntity.this.cookTime;
                    break;
                case 1:
                    var10000 = KettleBlockEntity.this.cookTimeTotal;
                    break;
                default:
                    var10000 = 0;
            }

            return var10000;
        }

        public void set(int index, int value) {
            if (index == 0) {
                KettleBlockEntity.this.cookTime = value;
            } else if (index == 1) {
                KettleBlockEntity.this.cookTimeTotal = value;
            }
        }

        public int getCount() {
            return 2;
        }
    }

    public boolean isWater() {
        if (this.level == null) {
            return false;
        } else {
            BlockState state = this.level.getBlockState(this.getBlockPos());
            int i = state.getValue(KettleBlock.WATER_LEVEL);
            boolean flag = i > 0;
            return flag;
        }
    }
}
