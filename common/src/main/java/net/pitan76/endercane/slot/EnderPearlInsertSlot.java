package net.pitan76.endercane.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.pitan76.endercane.EnderCane;
import net.pitan76.endercane.EnderCaneScreenHandler;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.CustomDataUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;

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
            NbtCompound nbt = NbtUtil.create();
            if (CustomDataUtil.hasNbt(handStack)) {
                nbt = CustomDataUtil.getNbt(handStack);
                if (NbtUtil.has(nbt, "ender_pearl"))
                    pearlCount = NbtUtil.getInt(nbt, "ender_pearl");
            }
            pearlCount += stack.getCount();
            NbtUtil.putInt(nbt, "ender_pearl", pearlCount);
            if (pearlCount > 0) {
                screenHandler.inventory.setStack(1, ItemStackUtil.create(Items.ENDER_PEARL, Math.min(16, pearlCount)));
            }
            CustomDataUtil.setNbt(handStack, nbt);
            super.callSetStack(ItemStackUtil.empty());
            return;
        }
        super.callSetStack(stack);
    }

    @Override
    public boolean canInsert(net.pitan76.mcpitanlib.midohra.item.ItemStack stack) {
        ItemStack handStack = screenHandler.handStack;
        EnderCane enderCane = (EnderCane) handStack.getItem();
        if (CustomDataUtil.hasNbt(handStack) && CustomDataUtil.has(handStack, "ender_pearl")) {
            NbtCompound nbt = CustomDataUtil.getNbt(handStack);
            int pearlCount = NbtUtil.getInt(nbt, "ender_pearl");
            if (pearlCount >= enderCane.getMaxPearlAmount()) return false;
        }
        return super.canInsert(stack);
    }
}
