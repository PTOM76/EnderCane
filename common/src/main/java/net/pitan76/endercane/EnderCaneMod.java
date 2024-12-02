package net.pitan76.endercane;

import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import net.pitan76.mcpitanlib.api.CommonModInitializer;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.ExtendedScreenHandlerTypeBuilder;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.network.v2.ServerNetworking;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
import net.pitan76.mcpitanlib.api.registry.result.SupplierResult;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.midohra.item.ItemGroups;

public class EnderCaneMod extends CommonModInitializer {
    public static final String MOD_ID = "endercane";
    public static final String MOD_NAME = "Ender Cane";

    public static EnderCaneMod INSTANCE;

    public static RegistryResult<Item> PURE_ENDER_CANE;
    public static RegistryResult<Item> MEDIUM_ENDER_CANE;
    public static RegistryResult<Item> ADVANCED_ENDER_CANE;
    public static RegistryResult<Item> INFINITY_ENDER_CANE;

    public static SupplierResult<ScreenHandlerType<EnderCaneScreenHandler>> ENDER_CANE_TYPE;

    public void init() {
        INSTANCE = this;

        PURE_ENDER_CANE = registry.registerItem(_id("ender_cane"), () -> new EnderCane(CompatibleItemSettings.of(_id("ender_cane")).addGroup(ItemGroups.TOOLS).maxCount(1), 64));
        MEDIUM_ENDER_CANE = registry.registerItem(_id("medium_ender_cane"), () -> new EnderCane(CompatibleItemSettings.of(_id("medium_ender_cane")).addGroup(ItemGroups.TOOLS).maxCount(1), 256));
        ADVANCED_ENDER_CANE = registry.registerItem(_id("advanced_ender_cane"), () -> new EnderCane(CompatibleItemSettings.of(_id("advanced_ender_cane")).addGroup(ItemGroups.TOOLS).maxCount(1), 1024));
        INFINITY_ENDER_CANE = registry.registerItem(_id("infinity_ender_cane"), () -> new EnderCane(CompatibleItemSettings.of(_id("infinity_ender_cane")).addGroup(ItemGroups.TOOLS).maxCount(1), -1));

        ENDER_CANE_TYPE = registry.registerScreenHandlerType(_id("ender_cane_gui"), new ExtendedScreenHandlerTypeBuilder<>(EnderCaneScreenHandler::new));

        ServerNetworking.registerReceiver(_id("add_point"), (e -> {
            BlockPos pos = e.buf.readBlockPos();
            Player player = e.player;
            if (player.getCurrentScreenHandler() instanceof EnderCaneScreenHandler) {
                EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) player.getCurrentScreenHandler();
                EnderCaneScreenHandler.addPoint(screenHandler, pos);
            }
        }));
        ServerNetworking.registerReceiver(_id("set_point"), (e -> {
            int index = e.buf.readInt();
            Player player = e.player;
            if (player.getCurrentScreenHandler() instanceof EnderCaneScreenHandler) {
                EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) player.getCurrentScreenHandler();
                EnderCaneScreenHandler.setPoint(screenHandler, index);
            }
        }));
        ServerNetworking.registerReceiver(_id("remove_point"), (e -> {
            int index = e.buf.readInt();
            Player player = e.player;
            if (player.getCurrentScreenHandler() instanceof EnderCaneScreenHandler) {
                EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) player.getCurrentScreenHandler();
                EnderCaneScreenHandler.removePoint(screenHandler, index);
            }
        }));
    }
    
    public static CompatIdentifier _id(String path) {
        return CompatIdentifier.of(MOD_ID, path);
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

    @Override
    public String getName() {
        return MOD_NAME;
    }
}