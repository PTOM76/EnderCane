package net.pitan76.endercane;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.container.factory.DisplayNameArgs;
import net.pitan76.mcpitanlib.api.event.container.factory.ExtraDataArgs;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.gui.ExtendedScreenHandlerFactory;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.ExtendItem;
import net.pitan76.mcpitanlib.api.util.ActionResultUtil;
import net.pitan76.mcpitanlib.api.util.CustomDataUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;

public class EnderCane extends ExtendItem {
    private final int maxPearlAmount;

    public EnderCane(CompatibleItemSettings settings, int maxPearlAmount) {
        super(settings);
        this.maxPearlAmount = maxPearlAmount;
    }

    @Override
    public TypedActionResult<ItemStack> onRightClick(ItemUseEvent e) {
        Player player = e.user;
        ItemStack stack = player.getPlayerEntity().getStackInHand(e.hand);

        if (e.isClient()) return ActionResultUtil.typedActionResult(ActionResult.SUCCESS, stack);

        if (e.isSneaking() || !(CustomDataUtil.hasNbt(stack) && CustomDataUtil.has(stack, "SelectPoint"))) {
            player.openExtendedMenu(new ExtendedScreenHandlerFactory() {
                @Override
                public Text getDisplayName(DisplayNameArgs args) {
                    return TextUtil.translatable("gui.endercane.ender_cane_container");
                }

                @Override
                public void writeExtraData(ExtraDataArgs args) {
                    args.writeVar(e.hand.name());
                }

                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new EnderCaneScreenHandler(syncId, inv, stack);
                }
            });

            return ActionResultUtil.typedActionResult(ActionResult.SUCCESS, stack);
        }

        if (CustomDataUtil.hasNbt(stack) && CustomDataUtil.has(stack, "SelectPoint")) {
            NbtCompound nbt = CustomDataUtil.getNbt(stack);
            int pearlCount = NbtUtil.get(nbt, "ender_pearl", Integer.class);
            if (pearlCount > 0 || getMaxPearlAmount() == -1) {
                NbtCompound point = NbtUtil.get(nbt, "SelectPoint");
                if (NbtUtil.has(point, "x") && NbtUtil.has(point, "y") && NbtUtil.has(point, "z")) {
                    player.teleport(NbtUtil.get(point, "x", Integer.class), NbtUtil.get(point, "y", Integer.class), NbtUtil.get(point, "z", Integer.class));

                    if (getMaxPearlAmount() != -1) {
                        pearlCount--;
                        NbtUtil.set(nbt, "ender_pearl", pearlCount);
                        CustomDataUtil.setNbt(stack, nbt);
                    }
                }
            }

            return ActionResultUtil.typedActionResult(ActionResult.SUCCESS, stack);
        }

        return ActionResultUtil.typedActionResult(ActionResult.PASS, stack);
    }

    @Override
    public ActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        Player player = e.player;
        ItemStack stack = player.getPlayerEntity().getStackInHand(e.hand);

        if (e.isClient()) return ActionResult.SUCCESS;

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
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new EnderCaneScreenHandler(syncId, inv, stack);
                }
            });

            return ActionResult.SUCCESS;
        }
        if (CustomDataUtil.hasNbt(stack) && CustomDataUtil.has(stack, "SelectPoint")) {
            NbtCompound nbt = CustomDataUtil.getNbt(stack);
            int pearlCount = NbtUtil.get(nbt, "ender_pearl", Integer.class);
            if (pearlCount > 0 || getMaxPearlAmount() == -1) {
                NbtCompound point = NbtUtil.get(nbt, "SelectPoint");
                if (NbtUtil.has(point, "x") && NbtUtil.has(point, "y") && NbtUtil.has(point, "z")) {
                    player.teleport(NbtUtil.get(point, "x", Integer.class), NbtUtil.get(point, "y", Integer.class), NbtUtil.get(point, "z", Integer.class));

                    if (getMaxPearlAmount() != -1) {
                        pearlCount--;
                        NbtUtil.set(nbt, "ender_pearl", pearlCount);
                        CustomDataUtil.setNbt(stack, nbt);
                    }
                }
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        if (CustomDataUtil.hasNbt(stack) && CustomDataUtil.has(stack, "ender_pearl")) {
            NbtCompound nbt = CustomDataUtil.getNbt(stack);
            int pearlCount = NbtUtil.get(nbt, "ender_pearl", Integer.class);
            return Math.round((float) pearlCount * 13.0F / (float) this.getMaxPearlAmount());
        }
        return 0;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        int pearlCount = 0;
        if (CustomDataUtil.hasNbt(stack) && CustomDataUtil.has(stack, "ender_pearl")) {
            NbtCompound nbt = CustomDataUtil.getNbt(stack);
            pearlCount = NbtUtil.get(nbt, "ender_pearl", Integer.class);
        }
        float f = 360 - 80 * Math.max(0.0F, (float)pearlCount / (float)this.getMaxPearlAmount());
        return MathHelper.hsvToRgb(f / 360, 1.0F, 1.0F);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return getMaxPearlAmount() != -1;
    }

    public int getMaxPearlAmount() {
        return maxPearlAmount;
    }
}
