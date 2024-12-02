package net.pitan76.endercane;

import net.pitan76.mcpitanlib.api.event.item.*;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.gui.v2.ExtendedScreenHandlerFactory;
import net.pitan76.mcpitanlib.api.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.container.factory.DisplayNameArgs;
import net.pitan76.mcpitanlib.api.event.container.factory.ExtraDataArgs;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;

public class EnderCane extends CompatItem  {
    private final int maxPearlAmount;

    public EnderCane(CompatibleItemSettings settings, int maxPearlAmount) {
        super(settings);
        this.maxPearlAmount = maxPearlAmount;
    }

    @Override
    public StackActionResult onRightClick(ItemUseEvent e) {
        Player player = e.getUser();
        ItemStack stack = e.getStack();

        if (e.isClient()) return e.success();

        if (e.isSneaking() || !(CustomDataUtil.hasNbt(stack) && CustomDataUtil.has(stack, "SelectPoint"))) {
            player.openExtendedMenu(new ExtendedScreenHandlerFactory() {
                @Override
                public Text getDisplayName(DisplayNameArgs args) {
                    return TextUtil.translatable("gui.endercane.ender_cane_container");
                }

                @Override
                public void writeExtraData(ExtraDataArgs args) {
                    args.writeVar(e.getHand().name());
                }

                @Override
                public ScreenHandler createMenu(CreateMenuEvent e) {
                    return new EnderCaneScreenHandler(e.getSyncId(), e.getPlayerInventory(), stack);
                }
            });

            return e.success();
        }

        if (CustomDataUtil.hasNbt(stack) && CustomDataUtil.has(stack, "SelectPoint")) {
            NbtCompound nbt = CustomDataUtil.getNbt(stack);
            int pearlCount = NbtUtil.getInt(nbt, "ender_pearl");
            if (pearlCount > 0 || getMaxPearlAmount() == -1) {
                NbtCompound point = NbtUtil.get(nbt, "SelectPoint");
                if (NbtUtil.has(point, "x") && NbtUtil.has(point, "y") && NbtUtil.has(point, "z")) {
                    player.teleport(NbtUtil.getInt(point, "x"), NbtUtil.getInt(point, "y"), NbtUtil.getInt(point, "z"));

                    if (getMaxPearlAmount() != -1) {
                        pearlCount--;
                        NbtUtil.putInt(nbt, "ender_pearl", pearlCount);
                        CustomDataUtil.setNbt(stack, nbt);
                    }
                }
            }

            return e.success();
        }

        return e.pass();
    }

    @Override
    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        Player player = e.getPlayer();
        ItemStack stack = e.getStack();

        if (e.isClient()) return e.success();

        if (player.isSneaking() || !(CustomDataUtil.hasNbt(stack) && CustomDataUtil.has(stack, "SelectPoint"))) {
            player.openExtendedMenu(new ExtendedScreenHandlerFactory() {
                @Override
                public Text getDisplayName(DisplayNameArgs args) {
                    return TextUtil.translatable("gui.endercane.ender_cane_container");
                }

                @Override
                public void writeExtraData(ExtraDataArgs args) {
                    args.writeVar(e.hand.name());
                    args.writeVar(e.getBlockPos());
                }

                @Override
                public ScreenHandler createMenu(CreateMenuEvent e) {
                    return new EnderCaneScreenHandler(e.getSyncId(), e.getPlayerInventory(), stack);
                }
            });

            return e.success();
        }
        if (CustomDataUtil.hasNbt(stack) && CustomDataUtil.has(stack, "SelectPoint")) {
            NbtCompound nbt = CustomDataUtil.getNbt(stack);
            int pearlCount = NbtUtil.getInt(nbt, "ender_pearl");
            if (pearlCount > 0 || getMaxPearlAmount() == -1) {
                NbtCompound point = NbtUtil.get(nbt, "SelectPoint");
                if (NbtUtil.has(point, "x") && NbtUtil.has(point, "y") && NbtUtil.has(point, "z")) {
                    player.teleport(NbtUtil.getInt(point, "x"), NbtUtil.getInt(point, "y"), NbtUtil.getInt(point, "z"));

                    if (getMaxPearlAmount() != -1) {
                        pearlCount--;
                        NbtUtil.putInt(nbt, "ender_pearl", pearlCount);
                        CustomDataUtil.setNbt(stack, nbt);
                    }
                }
            }

            return e.success();
        }

        return e.pass();
    }

    @Override
    public int getItemBarStep(ItemBarStepArgs args) {
        ItemStack stack = args.getStack();
        if (CustomDataUtil.hasNbt(stack) && CustomDataUtil.has(stack, "ender_pearl")) {
            NbtCompound nbt = CustomDataUtil.getNbt(stack);
            int pearlCount = NbtUtil.getInt(nbt, "ender_pearl");
            return Math.round((float) pearlCount * 13.0F / (float) this.getMaxPearlAmount());
        }
        return 0;
    }

    @Override
    public int getItemBarColor(ItemBarColorArgs args) {
        ItemStack stack = args.getStack();

        int pearlCount = 0;
        if (CustomDataUtil.hasNbt(stack) && CustomDataUtil.has(stack, "ender_pearl")) {
            NbtCompound nbt = CustomDataUtil.getNbt(stack);
            pearlCount = NbtUtil.getInt(nbt, "ender_pearl");
        }
        float f = 360 - 80 * Math.max(0.0F, (float)pearlCount / (float)this.getMaxPearlAmount());
        return MathHelper.hsvToRgb(f / 360, 1.0F, 1.0F);
    }

    @Override
    public boolean isItemBarVisible(ItemBarVisibleArgs args) {
        return getMaxPearlAmount() != -1;
    }

    public int getMaxPearlAmount() {
        return maxPearlAmount;
    }
}
