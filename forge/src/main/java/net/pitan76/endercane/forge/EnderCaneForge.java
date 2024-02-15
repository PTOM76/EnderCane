package net.pitan76.endercane.forge;

import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.pitan76.endercane.EnderCaneMod;
import net.pitan76.endercane.forge.client.EnderCaneForgeClient;

@Mod(EnderCaneMod.MOD_ID)
public class EnderCaneForge {
    public EnderCaneForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(EnderCaneMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        EnderCaneMod.init();

        if (Platform.getEnvironment().toPlatform() == Dist.CLIENT)
            FMLJavaModLoadingContext.get().getModEventBus().addListener(EnderCaneForgeClient::clientInit);
    }
}