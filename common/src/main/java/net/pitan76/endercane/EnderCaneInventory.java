package net.pitan76.endercane;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.util.CustomDataUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.api.util.inventory.CompatInventory;
import net.pitan76.mcpitanlib.api.util.inventory.args.CanInsertArgs;

public class EnderCaneInventory extends CompatInventory {
    public EnderCaneScreenHandler screenHandler;
    public EnderCaneInventory(EnderCaneScreenHandler screenHandler) {
        super(2);
        this.screenHandler = screenHandler;
    }

    @Override
    public boolean canInsert(CanInsertArgs args) {
        ItemStack handStack = screenHandler.handStack;
        EnderCane enderCane = (EnderCane) handStack.getItem();
        if (CustomDataUtil.hasNbt(handStack) && CustomDataUtil.has(handStack, "ender_pearl")) {
            NbtCompound nbt = CustomDataUtil.getNbt(handStack);
            int pearlCount = NbtUtil.getInt(nbt, "ender_pearl");
            if (pearlCount >= enderCane.getMaxPearlAmount()) return false;
        }
        return super.canInsert(args);
    }

    @Override
    public boolean canPlayerUse(Player player) {
        ItemStack handStack = screenHandler.handStack;
        EnderCane enderCane = (EnderCane) handStack.getItem();
        if (CustomDataUtil.hasNbt(handStack) && CustomDataUtil.has(handStack, "ender_pearl")) {
            NbtCompound nbt = CustomDataUtil.getNbt(handStack);
            int pearlCount = NbtUtil.getInt(nbt, "ender_pearl");
            if (pearlCount >= enderCane.getMaxPearlAmount()) return false;
        }
        return super.canPlayerUse(player);
    }
}
