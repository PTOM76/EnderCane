package ml.pkom.endercane;

import ml.pkom.endercane.client.EnderCaneClientMod;
import ml.pkom.mcpitanlibarch.api.item.DefaultItemGroups;
import ml.pkom.mcpitanlibarch.api.item.ExtendItem;
import ml.pkom.mcpitanlibarch.api.item.ExtendSettings;
import ml.pkom.mcpitanlibarch.api.registry.ArchRegistry;
import ml.pkom.mcpitanlibarch.api.util.PlatformUtil;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class EnderCaneMod {
    public static final String MOD_ID = "endercane";

    public static ArchRegistry registry = new ArchRegistry(MOD_ID);

    public static ScreenHandlerType<EnderCaneScreenHandler> ENDER_CANE_TYPE = new ExtendedScreenHandlerTypeBuilder<>(EnderCaneScreenHandler::new).build();
    public static Item ENDER_CANE = new ExtendItem(new ExtendSettings().addGroup(DefaultItemGroups.TOOLS, id(" ender_cane")).maxCount(1));

    public static void init() {

        registry.registerItem(id("ender_cane"), () -> ENDER_CANE);
        registry.registerScreenHandlerType(id("ender_cane_gui"), () -> ENDER_CANE_TYPE);
        registry.allRegister();

        if (PlatformUtil.isClient())
            EnderCaneClientMod.init();
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}