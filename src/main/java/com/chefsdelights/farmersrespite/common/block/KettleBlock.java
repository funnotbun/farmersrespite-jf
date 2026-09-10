package com.chefsdelights.farmersrespite.common.block;

import com.chefsdelights.farmersrespite.common.block.entity.KettleBlockEntity;
import com.chefsdelights.farmersrespite.common.block.entity.inventory.ItemHandler;
import com.chefsdelights.farmersrespite.core.registry.FRBlockEntityTypes;
import com.chefsdelights.farmersrespite.core.registry.FRSounds;
import com.chefsdelights.farmersrespite.core.utility.MathUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;
import vectorwing.farmersdelight.common.tag.ModTags;

@SuppressWarnings("deprecation")
public class KettleBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<KettleBlock> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(propertiesCodec()).apply(inst, KettleBlock::new));
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<CookingPotSupport> SUPPORT = EnumProperty.create("support", CookingPotSupport.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty WATER_LEVEL = IntegerProperty.create("water", 0, 3);
    public static final BooleanProperty LID = BooleanProperty.create("lid");

    protected static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 7.0D, 13.0D);
    protected static final VoxelShape SHAPE_WITH_TRAY = Shapes.or(SHAPE, Block.box(0.0D, -1.0D, 0.0D, 16.0D, 0.0D, 16.0D));

    public KettleBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SUPPORT, CookingPotSupport.NONE).setValue(WATERLOGGED, false).setValue(WATER_LEVEL, 0).setValue(LID, true));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public InteractionResult useItemOn(ItemStack heldStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult result) {
        Item item = heldStack.getItem();
        int i = state.getValue(WATER_LEVEL);
        if (!world.isClientSide() && player instanceof ServerPlayer) {
            if (heldStack.isEmpty() && player.isShiftKeyDown()) {
                if (state.getValue(LID)) {
                    world.setBlockAndUpdate(pos, state.setValue(LID, false));
                }
                if (!state.getValue(LID)) {
                    world.setBlockAndUpdate(pos, state.setValue(LID, true));
                }
                world.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.7F, 1.0F);
            } else if (i < 3 && item == Items.WATER_BUCKET) {
                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(handIn, new ItemStack(Items.BUCKET));
                }
                if (i == 0) {
                    world.setBlockAndUpdate(pos, state.setValue(WATER_LEVEL, i + 3));
                }
                if (i == 1) {
                    world.setBlockAndUpdate(pos, state.setValue(WATER_LEVEL, i + 2));
                }
                if (i == 2) {
                    world.setBlockAndUpdate(pos, state.setValue(WATER_LEVEL, i + 1));
                }
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_EMPTY, SoundSource.NEUTRAL, 1.0F, 1.0F);
            } else if (i < 3 && item == Items.POTION && isWaterPotion(heldStack)) {
                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(handIn, new ItemStack(Items.GLASS_BOTTLE));
                }
                world.setBlockAndUpdate(pos, state.setValue(WATER_LEVEL, i + 1));
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_EMPTY, SoundSource.NEUTRAL, 1.0F, 1.0F);
            } else if (world.getBlockEntity(pos) instanceof KettleBlockEntity kettleEntity) {
                ItemStack servingStack = kettleEntity.useHeldItemOnMeal(heldStack);
                if (servingStack != ItemStack.EMPTY) {
                    if (!player.getInventory().add(servingStack)) {
                        player.drop(servingStack, false);
                    }
                    world.playSound(null, pos, SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    player.openMenu(kettleEntity);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    private static boolean isWaterPotion(ItemStack stack) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        return contents != null && contents.is(Potions.WATER);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.getValue(SUPPORT).equals(CookingPotSupport.TRAY) ? SHAPE_WITH_TRAY : SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        FluidState fluid = world.getFluidState(context.getClickedPos());

        BlockState state = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);

        if (context.getClickedFace().equals(Direction.DOWN)) {
            return state.setValue(SUPPORT, CookingPotSupport.HANDLE);
        }
        return state.setValue(SUPPORT, getTrayState(world, pos));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (direction.getAxis().equals(Direction.Axis.Y) && !state.getValue(SUPPORT).equals(CookingPotSupport.HANDLE)) {
            return state.setValue(SUPPORT, getTrayState(level, pos));
        }
        return state;
    }

    private CookingPotSupport getTrayState(LevelReader world, BlockPos pos) {
        if (world.getBlockState(pos.below()).is(ModTags.TRAY_HEAT_SOURCES)) {
            return CookingPotSupport.TRAY;
        }
        return CookingPotSupport.NONE;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack stack = super.getCloneItemStack(level, pos, state, includeData);
        level.getBlockEntity(pos, FRBlockEntityTypes.KETTLE).ifPresent(kettleEntity -> {
            if (level instanceof Level actualLevel) {
                CompoundTag nbt = kettleEntity.writeMeal(new CompoundTag(), actualLevel.registryAccess());
                if (!nbt.isEmpty()) {
                    stack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.<BlockEntityType<?>>of(kettleEntity.getType(), nbt));
                }
            }
            if (kettleEntity.getCustomName() != null) {
                stack.set(DataComponents.CUSTOM_NAME, kettleEntity.getCustomName());
            }
        });
        return stack;
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean moving) {
        if (level.getBlockEntity(pos) instanceof KettleBlockEntity kettleEntity) {
            Containers.dropContents(level, pos, kettleEntity.getDroppableInventory());
            kettleEntity.grantStoredRecipeExperience(level, Vec3.atCenterOf(pos));
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.affectNeighborsAfterRemoval(state, level, pos, moving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, SUPPORT, WATERLOGGED, WATER_LEVEL, LID);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Component name = stack.get(DataComponents.CUSTOM_NAME);
        if (name != null && worldIn.getBlockEntity(pos) instanceof KettleBlockEntity kettleEntity) {
            kettleEntity.setCustomName(name);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof KettleBlockEntity kettleEntity && kettleEntity.isHeated() && stateIn.getValue(LID)) {
            double x = pos.getX() + 0.5D;
            double y = pos.getY();
            double z = pos.getZ() + 0.5D;
            if (rand.nextInt(20) == 0) {
                worldIn.playLocalSound(x, y, z, FRSounds.BLOCK_KETTLE_WHISTLE, SoundSource.BLOCKS, 0.07F, rand.nextFloat() * 0.2F + 0.9F, false);
            }
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos, Direction direction) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof KettleBlockEntity kettleEntity) {
            ItemHandler inventory = kettleEntity.getInventory();
            return MathUtils.calcRedstoneFromItemHandler(inventory);
        }
        return 0;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return FRBlockEntityTypes.KETTLE.create(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
        if (level.isClientSide()) {
            return createTickerHelper(blockEntity, FRBlockEntityTypes.KETTLE, KettleBlockEntity::animationTick);
        } else {
            return createTickerHelper(blockEntity, FRBlockEntityTypes.KETTLE, (menuLevel, pos, menuState, kettle) -> {
                if (menuLevel instanceof ServerLevel server) {
                    KettleBlockEntity.cookingTick(server, pos, menuState, kettle);
                }
            });
        }
    }
}
