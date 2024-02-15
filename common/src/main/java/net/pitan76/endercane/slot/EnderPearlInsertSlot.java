package net.pitan76.endercane.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.pitan76.endercane.EnderCane;
import net.pitan76.endercane.EnderCaneScreenHandler;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.nbt.NbtTag;

public class EnderPearlInsertSlot extends CompatibleSlot {
    public EnderCaneScreenHandler screenHandler;
    public EnderPearlInsertSlot(EnderCaneScreenHandler screenHandler, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.screenHandler = screenHandler;
    }

    @Override
    public void callSetStack(ItemStack stack) {
        if (stack.getItem() == Items.ENDER_PEARL) {
            ItemStack handStack = screenHandler.handStack;
            int pearlCount = 0;
            NbtCompound nbt = NbtTag.create();
            if (handStack.hasNbt()) {
                nbt = handStack.getNbt();
                if (nbt.contains("ender_pearl"))
                    pearlCount = nbt.getInt("ender_pearl");
            }
            pearlCount += stack.getCount();
            nbt.putInt("ender_pearl", pearlCount);
            if (pearlCount > 0) {
                screenHandler.inventory.setStack(1, new ItemStack(Items.ENDER_PEARL, Math.min(16, pearlCount)));
            }
            handStack.setNbt(nbt);
            super.callSetStack(ItemStack.EMPTY);
            return;
        }
        super.callSetStack(stack);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        ItemStack handStack = screenHandler.handStack;
        EnderCane enderCane = (EnderCane) handStack.getItem();
        if (handStack.hasNbt() && handStack.getNbt().contains("ender_pearl")) {
            NbtCompound nbt = handStack.getNbt();
            int pearlCount = nbt.getInt("ender_pearl");
            if (pearlCount >= enderCane.getMaxPearlAmount()) return false;
        }
        return super.canInsert(stack);
    }
}
