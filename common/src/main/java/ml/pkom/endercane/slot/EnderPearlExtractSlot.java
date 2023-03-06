package ml.pkom.endercane.slot;

import ml.pkom.endercane.EnderCaneScreenHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;

public class EnderPearlExtractSlot extends Slot {
    public EnderCaneScreenHandler screenHandler;
    public EnderPearlExtractSlot(EnderCaneScreenHandler screenHandler, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.screenHandler = screenHandler;
    }

    @Override
    public void setStack(ItemStack stack) {
        super.setStack(stack);
    }

    @Override
    public ItemStack takeStack(int amount) {
        ItemStack handStack = screenHandler.handStack;
        int pearlCount = 0;
        NbtCompound nbt = new NbtCompound();
        if (handStack.hasNbt()) {
            nbt = handStack.getNbt();
            if (nbt.contains("ender_pearl"))
                pearlCount = nbt.getInt("ender_pearl");
        }
        pearlCount -= amount;
        nbt.putInt("ender_pearl", pearlCount);
        ItemStack takeStack = super.takeStack(amount);
        if (pearlCount > 0) {
            setStack(new ItemStack(Items.ENDER_PEARL, Math.min(16, pearlCount)));
        }
        handStack.setNbt(nbt);

        return takeStack;
    }

    @Override
    public ItemStack getStack() {
        return super.getStack();
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }
}
