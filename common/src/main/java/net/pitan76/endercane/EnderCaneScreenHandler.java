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
import net.pitan76.mcpitanlib.api.nbt.NbtTag;
import net.pitan76.mcpitanlib.api.util.SlotUtil;

public class EnderCaneScreenHandler extends ExtendedScreenHandler {

    public final Inventory inventory = new EnderCaneInventory(this);

    public ItemStack handStack;
    public BlockPos pos;

    public EnderCaneScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, ItemStack.EMPTY);
        String handStr = buf.readString();
        if (buf.isReadable())
            pos = buf.readBlockPos();
        handStack = playerInventory.player.getStackInHand(Hand.valueOf(handStr));
    }

    public EnderCaneScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack stack) {
        super(EnderCaneMod.ENDER_CANE_TYPE.get(), syncId);
        handStack = stack;
        pos = new Player(playerInventory.player).getBlockPos();

        addPlayerHotbarSlots(playerInventory, 8, 142);
        addPlayerMainInventorySlots(playerInventory, 8, 84);

        if (handStack.isEmpty() || ((EnderCane) handStack.getItem()).getMaxPearlAmount() != -1) {
            addSlot(new EnderPearlInsertSlot(this, inventory, 0, 15, 47));
            Slot slot = new EnderPearlExtractSlot(this, inventory, 1, 35, 47);
            addSlot(slot);

            if (handStack.hasNbt() && handStack.getNbt().contains("ender_pearl")) {
                NbtCompound nbt = handStack.getNbt();
                int pearlCount = nbt.getInt("ender_pearl");
                if (pearlCount > 0)
                    SlotUtil.setStack(slot, new ItemStack(Items.ENDER_PEARL, Math.min(16, pearlCount)));
            }
        }
    }

    @Override
    public ItemStack quickMoveOverride(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();

            if (index == 37 && itemStack.getItem() == Items.ENDER_PEARL) {
                int pearlCount = 0;
                NbtCompound nbt = NbtTag.create();
                if (handStack.hasNbt()) {
                    nbt = handStack.getNbt();
                    if (nbt.contains("ender_pearl"))
                        pearlCount = nbt.getInt("ender_pearl");
                }
                pearlCount -= itemStack.getCount();
                nbt.putInt("ender_pearl", pearlCount);
                if (pearlCount > 0) {
                    inventory.setStack(1, new ItemStack(Items.ENDER_PEARL, Math.min(16, pearlCount)));
                }
                handStack.setNbt(nbt);
            }
            if (index < 36) {
                int pearlCount = 0;
                if (handStack.hasNbt()) {
                    NbtCompound nbt = handStack.getNbt();
                    if (nbt.contains("ender_pearl"))
                        pearlCount = nbt.getInt("ender_pearl");
                }
                if (pearlCount + itemStack2.getCount() >= ((EnderCane) handStack.getItem()).getMaxPearlAmount() || !this.callInsertItem(itemStack2, 36, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.callInsertItem(itemStack2, 0, 35, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                SlotUtil.setStack(slot, ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player.getPlayerEntity(), itemStack2);
        }

        return itemStack;
    }

    public static void addPoint(EnderCaneScreenHandler screenHandler, BlockPos pos) {
        NbtCompound nbt = NbtTag.create();
        if (screenHandler.handStack.hasNbt()) {
            nbt = screenHandler.handStack.getNbt();
        }
        NbtList points = new NbtList();
        if (nbt.contains("Points"))
            points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);

        NbtCompound posNbt = NbtTag.create();
        posNbt.putInt("x", pos.getX());
        posNbt.putInt("y", pos.getY());
        posNbt.putInt("z", pos.getZ());
        points.add(posNbt);

        nbt.put("Points", points);

        screenHandler.handStack.setNbt(nbt);
    }

    public static void setPoint(EnderCaneScreenHandler screenHandler, int index) {
        if (screenHandler.handStack.hasNbt()) {
            NbtCompound nbt = screenHandler.handStack.getNbt();

            NbtList points = new NbtList();
            if (nbt.contains("Points"))
                points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);

            int i = 0;
            for (NbtElement q : points) {
                if (i == index) {
                    NbtCompound point = (NbtCompound) q;
                    nbt.put("SelectPoint", point);
                    break;
                }
                i++;
            }

            screenHandler.handStack.setNbt(nbt);
        }
    }

    public static void removePoint(EnderCaneScreenHandler screenHandler, int index) {
        if (screenHandler.handStack.hasNbt()) {
            NbtCompound nbt = screenHandler.handStack.getNbt();

            NbtList points = new NbtList();
            if (nbt.contains("Points"))
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

            nbt.put("Points", points);

            screenHandler.handStack.setNbt(nbt);
        }
    }
}
