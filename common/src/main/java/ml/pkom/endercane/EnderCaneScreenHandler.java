package ml.pkom.endercane;

import ml.pkom.mcpitanlibarch.api.gui.ExtendedScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;

public class EnderCaneScreenHandler extends ExtendedScreenHandler {

    private final Inventory inventory = new SimpleInventory(2);

    public EnderCaneScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory);
    }

    public EnderCaneScreenHandler(int syncId, PlayerInventory inventory) {
        super(type, syncId);

        addPlayerHotbarSlots(inventory, 8, 142);
        addPlayerMainInventorySlots(inventory, 8, 84);

        addNormalSlot(inventory, 0, 15, 47);
        addNormalSlot(inventory, 1, 35, 47);
    }
}
