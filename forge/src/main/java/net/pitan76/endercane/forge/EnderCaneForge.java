package net.pitan76.endercane.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.pitan76.endercane.EnderCaneMod;
import net.pitan76.endercane.forge.client.EnderCaneForgeClient;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;

@Mod(EnderCaneMod.MOD_ID)
public class EnderCaneForge {
    public EnderCaneForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(EnderCaneMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        new EnderCaneMod();

        if (PlatformUtil.isClient())
            FMLJavaModLoadingContext.get().getModEventBus().addListener(EnderCaneForgeClient::clientInit);
    }
}