package net.pitan76.endercane;

import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.pitan76.mcpitanlib.api.gui.ExtendedScreenHandlerTypeBuilder;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.DefaultItemGroups;
import net.pitan76.mcpitanlib.api.network.ServerNetworking;
import net.pitan76.mcpitanlib.api.registry.CompatRegistry;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;

public class EnderCaneMod {
    public static final String MOD_ID = "endercane";

    public static CompatRegistry registry = new CompatRegistry(MOD_ID);

    public static RegistryResult<Item> PURE_ENDER_CANE;
    public static RegistryResult<Item> MEDIUM_ENDER_CANE;
    public static RegistryResult<Item> ADVANCED_ENDER_CANE;
    public static RegistryResult<Item> INFINITY_ENDER_CANE;

    public static RegistryResult<ScreenHandlerType<?>> ENDER_CANE_TYPE;

    public static void init() {
        PURE_ENDER_CANE = registry.registerItem(id("ender_cane"), () -> new EnderCane(new CompatibleItemSettings().addGroup(DefaultItemGroups.TOOLS, id("ender_cane")).maxCount(1), 64));
        MEDIUM_ENDER_CANE = registry.registerItem(id("medium_ender_cane"), () -> new EnderCane(new CompatibleItemSettings().addGroup(DefaultItemGroups.TOOLS, id("medium_ender_cane")).maxCount(1), 256));
        ADVANCED_ENDER_CANE = registry.registerItem(id("advanced_ender_cane"), () -> new EnderCane(new CompatibleItemSettings().addGroup(DefaultItemGroups.TOOLS, id("advanced_ender_cane")).maxCount(1), 1024));
        INFINITY_ENDER_CANE = registry.registerItem(id("infinity_ender_cane"), () -> new EnderCane(new CompatibleItemSettings().addGroup(DefaultItemGroups.TOOLS, id("infinity_ender_cane")).maxCount(1), -1));

        ENDER_CANE_TYPE = registry.registerScreenHandlerType(id("ender_cane_gui"), () -> new ExtendedScreenHandlerTypeBuilder<>(EnderCaneScreenHandler::new).build());

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

        registry.allRegister();
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}