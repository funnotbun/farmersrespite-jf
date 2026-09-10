package com.chefsdelights.farmersrespite.common.block.entity.inventory;

import org.jetbrains.annotations.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ItemStackHandler implements ItemHandler {

    protected NonNullList<ItemStack> inventory;

    public ItemStackHandler() {
        this(1);
    }

    public ItemStackHandler(int inventorySize) {
        inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean canItemStacksStack(ItemStack left, ItemStack right) {
        return !left.isEmpty() && ItemStack.isSameItemSameComponents(left, right);
    }

    public static ItemStack copyStackWithNewSize(ItemStack itemStack, int newSize) {
        if (newSize == 0) {
            return ItemStack.EMPTY;
        }

        ItemStack copy = itemStack.copy();
        copy.setCount(newSize);

        return copy;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        validateSlotIndex(slot);
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return extractItemStack(slot, amount, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = getItem(slot);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return extractItemStack(slot, stack.getCount(), false);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        validateSlotIndex(slot);
        inventory.set(slot, stack);
        onInventorySlotChanged(slot);
    }

    @Override
    public void setChanged() {
        // Do nothing when the itemstack handler is marked as dirty
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    public ItemStack insertItemStack(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty() || !canPlaceItem(slot, stack)) {
            return stack;
        }

        validateSlotIndex(slot);

        ItemStack invItemStack = inventory.get(slot);
        int limit = getStackLimit(slot, invItemStack);

        if (!invItemStack.isEmpty()) {
            if (!canItemStacksStack(stack, invItemStack)) {
                return stack;
            }

            limit -= invItemStack.getCount();
        }

        if (limit <= 0) {
            return stack;
        }

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (invItemStack.isEmpty()) {
                inventory.set(slot, reachedLimit ? copyStackWithNewSize(stack, limit) : stack);
            } else {
                invItemStack.grow(reachedLimit ? limit : stack.getCount());
            }
            onInventorySlotChanged(slot);
        }

        return reachedLimit ? copyStackWithNewSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItemStack(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        }

        validateSlotIndex(slot);

        ItemStack invItemStack = inventory.get(slot);

        if (invItemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int nbrToExtract = Math.min(amount, invItemStack.getMaxStackSize());

        if (invItemStack.getCount() <= nbrToExtract) {
            if (!simulate) {
                inventory.set(slot, ItemStack.EMPTY);
                onInventorySlotChanged(slot);

                return invItemStack;
            } else {
                return invItemStack.copy();
            }
        } else {
            if (!simulate) {
                inventory.set(slot, copyStackWithNewSize(invItemStack, invItemStack.getCount() - nbrToExtract));
                onInventorySlotChanged(slot);
            }

            return copyStackWithNewSize(invItemStack, nbrToExtract);
        }
    }

    @Override
    public int getMaxCountForSlot(int slot) {
        return 64;
    }

    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    protected void onInventoryLoaded() {
        // Do nothing on basic itemstack handler when inventory is loaded
    }

    protected void onInventorySlotChanged(int slot) {
        // Do nothing on basic itemstack handler when inventory slot is changed
    }

    public void setSize(int size) {
        inventory = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= inventory.size())
            throw new IndexOutOfBoundsException("Slot " + slot + " not in valid range [0," + inventory.size() + ")");
    }

    protected int getStackLimit(int slot, ItemStack stack) {
        return Math.min(getMaxCountForSlot(slot), stack.getMaxStackSize());
    }

    public void serialize(ValueOutput output) {
        ContainerHelper.saveAllItems(output, inventory);
    }

    public void deserialize(ValueInput input) {
        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, ItemStack.EMPTY);
        }
        ContainerHelper.loadAllItems(input, inventory);
        onInventoryLoaded();
    }
}