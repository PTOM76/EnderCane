package net.pitan76.endercane;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class EnderCaneInventory extends SimpleInventory {
    public EnderCaneScreenHandler screenHandler;
    public EnderCaneInventory(EnderCaneScreenHandler screenHandler) {
        super(2);
        this.screenHandler = screenHandler;
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

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        ItemStack handStack = screenHandler.handStack;
        EnderCane enderCane = (EnderCane) handStack.getItem();
        if (handStack.hasNbt() && handStack.getNbt().contains("ender_pearl")) {
            NbtCompound nbt = handStack.getNbt();
            int pearlCount = nbt.getInt("ender_pearl");
            if (pearlCount >= enderCane.getMaxPearlAmount()) return false;
        }
        return super.canPlayerUse(player);
    }
}
