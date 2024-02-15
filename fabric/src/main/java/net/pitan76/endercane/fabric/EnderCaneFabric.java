package net.pitan76.endercane.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.pitan76.endercane.EnderCaneMod;
import net.pitan76.endercane.client.EnderCaneClientMod;

public class EnderCaneFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        EnderCaneMod.init();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            EnderCaneClientMod.init();
    }
}