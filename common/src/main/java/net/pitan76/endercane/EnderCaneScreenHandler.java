package net.pitan76.endercane;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.pitan76.endercane.slot.EnderPearlExtractSlot;
import net.pitan76.endercane.slot.EnderPearlInsertSlot;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.ExtendedScreenHandler;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.util.*;

public class EnderCaneScreenHandler extends ExtendedScreenHandler {

    public final Inventory inventory = new EnderCaneInventory(this);

    public ItemStack handStack;
    public BlockPos pos;

    public EnderCaneScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, ItemStackUtil.empty());
        String handStr = PacketByteUtil.readString(buf);
        if (buf.isReadable())
            pos = PacketByteUtil.readBlockPos(buf);
        
        Player player = new Player(playerInventory.player);
        handStack = player.getStackInHand(Hand.valueOf(handStr));
    }

    public EnderCaneScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack stack) {
        super(EnderCaneMod.ENDER_CANE_TYPE.get(), syncId);
        Player player = new Player(playerInventory.player);
        
        handStack = stack;
        pos = player.getBlockPos();

        addPlayerHotbarSlots(playerInventory, 8, 142);
        addPlayerMainInventorySlots(playerInventory, 8, 84);

        if (!ItemStackUtil.isEmpty(handStack) && ((EnderCane) handStack.getItem()).getMaxPearlAmount() == -1) return;

        callAddSlot(new EnderPearlInsertSlot(this, inventory, 0, 15, 47));

        CompatibleSlot extractSlot = new EnderPearlExtractSlot(this, inventory, 1, 35, 47);
        callAddSlot(extractSlot);

        if (CustomDataUtil.hasNbt(handStack) && CustomDataUtil.has(handStack, "ender_pearl")) {
            NbtCompound nbt = CustomDataUtil.getNbt(handStack);
            int pearlCount = NbtUtil.get(nbt, "ender_pearl", Integer.class);
            if (pearlCount > 0)
                SlotUtil.setStack(extractSlot, ItemStackUtil.create(Items.ENDER_PEARL, Math.min(16, pearlCount)));
        }
    }

    @Override
    public ItemStack quickMoveOverride(Player player, int index) {
        ItemStack itemStack = ItemStackUtil.empty();

        Slot slot = ScreenHandlerUtil.getSlot(this, index);
        if (SlotUtil.hasStack(slot)) {
            ItemStack itemStack2 = SlotUtil.getStack(slot);
            itemStack = itemStack2.copy();

            if (index == 37 && itemStack.getItem() == Items.ENDER_PEARL) {
                int pearlCount = 0;
                NbtCompound nbt = NbtUtil.create();
                if (CustomDataUtil.hasNbt(handStack)) {
                    nbt = CustomDataUtil.getNbt(handStack);
                    if (NbtUtil.has(nbt, "ender_pearl"))
                        pearlCount = NbtUtil.get(nbt, "ender_pearl", Integer.class);
                }
                pearlCount -= itemStack.getCount();
                NbtUtil.set(nbt, "ender_pearl", pearlCount);
                if (pearlCount > 0) {
                    inventory.setStack(1, new ItemStack(Items.ENDER_PEARL, Math.min(16, pearlCount)));
                }
                CustomDataUtil.setNbt(handStack, nbt);
            }
            if (index < 36) {
                int pearlCount = 0;
                if (CustomDataUtil.hasNbt(handStack)) {
                    NbtCompound nbt = CustomDataUtil.getNbt(handStack);
                    if (NbtUtil.has(nbt, "ender_pearl"))
                        pearlCount = NbtUtil.get(nbt, "ender_pearl", Integer.class);
                }
                if (pearlCount + itemStack2.getCount() >= ((EnderCane) handStack.getItem()).getMaxPearlAmount() || !this.callInsertItem(itemStack2, 36, 37, false)) {
                    return ItemStackUtil.empty();
                }
            } else if (!this.callInsertItem(itemStack2, 0, 35, false)) {
                return ItemStackUtil.empty();
            }

            if (itemStack2.isEmpty()) {
                SlotUtil.setStack(slot, ItemStackUtil.empty());
            } else {
                SlotUtil.markDirty(slot);
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStackUtil.empty();
            }

            SlotUtil.onTakeItem(slot, player, itemStack2);
        }

        return itemStack;
    }

    public static void addPoint(EnderCaneScreenHandler screenHandler, BlockPos pos) {
        NbtCompound nbt = NbtUtil.create();
        if (CustomDataUtil.hasNbt(screenHandler.handStack)) {
            nbt = CustomDataUtil.getNbt(screenHandler.handStack);
        }
        NbtList points = new NbtList();
        if (NbtUtil.has(nbt, "Points"))
            points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);

        NbtCompound posNbt = NbtUtil.create();
        NbtUtil.set(posNbt, "x", pos.getX());
        NbtUtil.set(posNbt, "y", pos.getY());
        NbtUtil.set(posNbt, "z", pos.getZ());
        
        points.add(posNbt);

        NbtUtil.set(nbt, "Points", points);

        CustomDataUtil.setNbt(screenHandler.handStack, nbt);
    }

    public static void setPoint(EnderCaneScreenHandler screenHandler, int index) {
        if (CustomDataUtil.hasNbt(screenHandler.handStack)) {
            NbtCompound nbt = CustomDataUtil.getNbt(screenHandler.handStack);

            NbtList points = new NbtList();
            if (NbtUtil.has(nbt, "Points"))
                points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);

            int i = 0;
            for (NbtElement q : points) {
                if (i == index) {
                    NbtCompound point = (NbtCompound) q;
                    NbtUtil.set(nbt, "SelectPoint", point);
                    break;
                }
                i++;
            }

            CustomDataUtil.setNbt(screenHandler.handStack, nbt);
        }
    }

    public static void removePoint(EnderCaneScreenHandler screenHandler, int index) {
        if (CustomDataUtil.hasNbt(screenHandler.handStack)) {
            NbtCompound nbt = CustomDataUtil.getNbt(screenHandler.handStack);

            NbtList points = new NbtList();
            if (NbtUtil.has(nbt, "Points"))
                points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);

            int i = 0;
            for (NbtElement q : points) {
                if (i == index) {
                    NbtCompound point = (NbtCompound) q;
                    points.remove(point);
                    break;
                }
                i++;
            }

            NbtUtil.set(nbt, "Points", points);

            CustomDataUtil.setNbt(screenHandler.handStack, nbt);
        }
    }
}
