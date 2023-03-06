package ml.pkom.endercane.fabric;

import ml.pkom.endercane.EnderCaneMod;
import ml.pkom.endercane.client.EnderCaneClientMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class EnderCaneFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        EnderCaneMod.init();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            EnderCaneClientMod.init();
    }
}