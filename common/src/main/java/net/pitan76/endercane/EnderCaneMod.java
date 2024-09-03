package net.pitan76.endercane;

import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import net.pitan76.mcpitanlib.api.CommonModInitializer;
import net.pitan76.mcpitanlib.api.gui.ExtendedScreenHandlerTypeBuilder;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.DefaultItemGroups;
import net.pitan76.mcpitanlib.api.network.ServerNetworking;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;

public class EnderCaneMod extends CommonModInitializer {
    public static final String MOD_ID = "endercane";
    public static final String MOD_NAME = "Ender Cane";

    public static EnderCaneMod INSTANCE;

    public static RegistryResult<Item> PURE_ENDER_CANE;
    public static RegistryResult<Item> MEDIUM_ENDER_CANE;
    public static RegistryResult<Item> ADVANCED_ENDER_CANE;
    public static RegistryResult<Item> INFINITY_ENDER_CANE;

    public static RegistryResult<ScreenHandlerType<?>> ENDER_CANE_TYPE;

    public void init() {
        INSTANCE = this;

        PURE_ENDER_CANE = registry.registerItem(compatId("ender_cane"), () -> new EnderCane(new CompatibleItemSettings().addGroup(DefaultItemGroups.TOOLS, id("ender_cane")).maxCount(1), 64));
        MEDIUM_ENDER_CANE = registry.registerItem(compatId("medium_ender_cane"), () -> new EnderCane(new CompatibleItemSettings().addGroup(DefaultItemGroups.TOOLS, id("medium_ender_cane")).maxCount(1), 256));
        ADVANCED_ENDER_CANE = registry.registerItem(compatId("advanced_ender_cane"), () -> new EnderCane(new CompatibleItemSettings().addGroup(DefaultItemGroups.TOOLS, id("advanced_ender_cane")).maxCount(1), 1024));
        INFINITY_ENDER_CANE = registry.registerItem(compatId("infinity_ender_cane"), () -> new EnderCane(new CompatibleItemSettings().addGroup(DefaultItemGroups.TOOLS, id("infinity_ender_cane")).maxCount(1), -1));

        ENDER_CANE_TYPE = registry.registerScreenHandlerType(compatId("ender_cane_gui"), () -> new ExtendedScreenHandlerTypeBuilder<>(EnderCaneScreenHandler::new).build());

        ServerNetworking.registerReceiver(id("add_point"), ((server, player, buf) -> {
            BlockPos pos = buf.readBlockPos();
            if (player.currentScreenHandler instanceof EnderCaneScreenHandler) {
                EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) player.currentScreenHandler;
                EnderCaneScreenHandler.addPoint(screenHandler, pos);
            }
        }));
        ServerNetworking.registerReceiver(id("set_point"), ((server, player, buf) -> {
            int index = buf.readInt();
            if (player.currentScreenHandler instanceof EnderCaneScreenHandler) {
                EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) player.currentScreenHandler;
                EnderCaneScreenHandler.setPoint(screenHandler, index);
            }
        }));
        ServerNetworking.registerReceiver(id("remove_point"), ((server, player, buf) -> {
            int index = buf.readInt();
            if (player.currentScreenHandler instanceof EnderCaneScreenHandler) {
                EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) player.currentScreenHandler;
                EnderCaneScreenHandler.removePoint(screenHandler, index);
            }
        }));
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