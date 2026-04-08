package net.pitan76.endercane.slot;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.pitan76.endercane.EnderCaneScreenHandler;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.CustomDataUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.api.util.inventory.ICompatInventory;
import net.pitan76.mcpitanlib.midohra.item.MCItems;

public class EnderPearlExtractSlot extends CompatibleSlot {
    public EnderCaneScreenHandler screenHandler;

    public EnderPearlExtractSlot(EnderCaneScreenHandler screenHandler, ICompatInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.screenHandler = screenHandler;
    }

    @Override
    public void callSetStack(ItemStack stack) {
        super.callSetStack(stack);
    }

    @Override
    public ItemStack callTakeStack(int amount) {
        ItemStack handStack = screenHandler.handStack;
        int pearlCount = 0;
        NbtCompound nbt = NbtUtil.create();
        if (CustomDataUtil.hasNbt(handStack)) {
            nbt = CustomDataUtil.getNbt(handStack);
            if (NbtUtil.has(nbt, "ender_pearl"))
                pearlCount = NbtUtil.getInt(nbt, "ender_pearl");
        }
        pearlCount -= amount;
        NbtUtil.putInt(nbt, "ender_pearl", pearlCount);
        ItemStack takeStack = super.callTakeStack(amount);
        if (pearlCount > 0) {
            callSetStack(MCItems.ENDER_PEARL.createStack(Math.min(16, pearlCount)).toMinecraft());
        }
        CustomDataUtil.setNbt(handStack, nbt);

        return takeStack;
    }

    @Override
    public ItemStack callGetStack() {
        return super.callGetStack();
    }

    @Override
    public boolean canInsert(net.pitan76.mcpitanlib.midohra.item.ItemStack stack) {
        return false;
    }
}
