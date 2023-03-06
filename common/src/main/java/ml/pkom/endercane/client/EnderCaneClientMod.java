package ml.pkom.endercane.client;

import ml.pkom.endercane.EnderCaneMod;
import ml.pkom.mcpitanlibarch.api.client.registry.ArchRegistryClient;

public class EnderCaneClientMod {
    public static void init() {
        ArchRegistryClient.registerScreen(EnderCaneMod.ENDER_CANE_TYPE, EnderCaneScreen::new);
    }
}
