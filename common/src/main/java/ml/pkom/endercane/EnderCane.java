package ml.pkom.endercane;

import dev.architectury.registry.menu.MenuRegistry;
import ml.pkom.mcpitanlibarch.api.entity.Player;
import ml.pkom.mcpitanlibarch.api.event.item.ItemUseEvent;
import ml.pkom.mcpitanlibarch.api.event.item.ItemUseOnBlockEvent;
import ml.pkom.mcpitanlibarch.api.gui.ExtendedNamedScreenHandlerFactory;
import ml.pkom.mcpitanlibarch.api.item.ExtendItem;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;

public class EnderCane extends ExtendItem {
    private int maxPearlAmount;

    public EnderCane(Settings settings, int maxPearlAmount) {
        super(settings);
        this.maxPearlAmount = maxPearlAmount;
    }

    @Override
    public TypedActionResult<ItemStack> onRightClick(ItemUseEvent event) {
        Player player = event.user;
        ItemStack stack = player.getPlayerEntity().getStackInHand(event.hand);

        if (!event.world.isClient())
            if (player.isSneaking() || !(stack.hasNbt() && stack.getNbt().contains("SelectPoint"))) {
                MenuRegistry.openExtendedMenu((ServerPlayerEntity) player.getPlayerEntity(), new ExtendedNamedScreenHandlerFactory(TextUtil.translatable("gui.endercane.ender_cane_container"), (syncId, inv, p) -> new EnderCaneScreenHandler(syncId, inv, stack), (buf) -> {
                    buf.writeString(event.hand.name());
                }));
            } else if (stack.hasNbt() && stack.getNbt().contains("SelectPoint")) {
                NbtCompound nbt = stack.getNbt();
                int pearlCount = nbt.getInt("ender_pearl");
                if (pearlCount > 0) {
                    NbtCompound point = nbt.getCompound("SelectPoint");
                    if (point.contains("x") && point.contains("y") && point.contains("z")) {
                        player.getEntity().teleport(point.getInt("x"), point.getInt("y"), point.getInt("z"));
                        pearlCount--;
                        nbt.putInt("ender_pearl", pearlCount);
                    }
                }
            }
        return TypedActionResult.success(stack);
    }

    @Override
    public ActionResult onRightClickOnBlock(ItemUseOnBlockEvent event) {
        Player player = event.player;
        ItemStack stack = player.getPlayerEntity().getStackInHand(event.hand);

        if (!event.world.isClient())
            if (player.isSneaking() || !(stack.hasNbt() && stack.getNbt().contains("SelectPoint"))) {
                MenuRegistry.openExtendedMenu((ServerPlayerEntity) player.getPlayerEntity(), new ExtendedNamedScreenHandlerFactory(TextUtil.translatable("gui.endercane.ender_cane_container"), (syncId, inv, p) -> new EnderCaneScreenHandler(syncId, inv, stack), (buf) -> {
                    buf.writeString(event.hand.name());
                    buf.writeBlockPos(event.hit.getBlockPos());
                }));
            } else if (stack.hasNbt() && stack.getNbt().contains("SelectPoint")) {
                NbtCompound nbt = stack.getNbt();
                int pearlCount = nbt.getInt("ender_pearl");
                if (pearlCount > 0) {
                    NbtCompound point = nbt.getCompound("SelectPoint");
                    if (point.contains("x") && point.contains("y") && point.contains("z")) {
                        player.getEntity().teleport(point.getInt("x"), point.getInt("y"), point.getInt("z"));
                        pearlCount--;
                        nbt.putInt("ender_pearl", pearlCount);
                    }
                }
            }
        return ActionResult.SUCCESS;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains("ender_pearl")) {
            NbtCompound nbt = stack.getNbt();
            int pearlCount = nbt.getInt("ender_pearl");
            return Math.round((float) pearlCount * 13.0F / (float) this.getMaxPearlAmount());
        }
        return 0;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        int pearlCount = 0;
        if (stack.hasNbt() && stack.getNbt().contains("ender_pearl")) {
            NbtCompound nbt = stack.getNbt();
            pearlCount = nbt.getInt("ender_pearl");
        }
        float f = 360 - 80 * Math.max(0.0F, (float)pearlCount / (float)this.getMaxPearlAmount());
        return MathHelper.hsvToRgb(f / 360, 1.0F, 1.0F);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    public int getMaxPearlAmount() {
        return maxPearlAmount;
    }
}
