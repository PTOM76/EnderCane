package net.pitan76.endercane.client;

import net.pitan76.endercane.EnderCaneMod;
import net.pitan76.mcpitanlib.api.client.registry.ArchRegistryClient;

public class EnderCaneClientMod {
    public static void init() {
        ArchRegistryClient.registerScreen(EnderCaneMod.ENDER_CANE_TYPE.getOrNull(), EnderCaneScreen::new);
    }
}
