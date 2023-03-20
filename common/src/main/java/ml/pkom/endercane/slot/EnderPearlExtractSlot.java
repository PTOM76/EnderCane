package ml.pkom.endercane.slot;

import ml.pkom.endercane.EnderCaneScreenHandler;
import ml.pkom.mcpitanlibarch.api.gui.slot.CompatibleSlot;
import ml.pkom.mcpitanlibarch.api.nbt.NbtTag;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

public class EnderPearlExtractSlot extends CompatibleSlot {
    public EnderCaneScreenHandler screenHandler;
    public EnderPearlExtractSlot(EnderCaneScreenHandler screenHandler, Inventory inventory, int index, int x, int y) {
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
        NbtCompound nbt = NbtTag.create();
        if (handStack.hasNbt()) {
            nbt = handStack.getNbt();
            if (nbt.contains("ender_pearl"))
                pearlCount = nbt.getInt("ender_pearl");
        }
        pearlCount -= amount;
        nbt.putInt("ender_pearl", pearlCount);
        ItemStack takeStack = super.callTakeStack(amount);
        if (pearlCount > 0) {
            callSetStack(new ItemStack(Items.ENDER_PEARL, Math.min(16, pearlCount)));
        }
        handStack.setNbt(nbt);

        return takeStack;
    }

    @Override
    public ItemStack callGetStack() {
        return super.callGetStack();
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }
}
