package ml.pkom.endercane.fabric;

import ml.pkom.endercane.EnderCaneMod;
import net.fabricmc.api.ModInitializer;

public class EnderCaneFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        EnderCaneMod.init();
    }
}