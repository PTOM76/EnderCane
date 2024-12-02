package net.pitan76.endercane.client;

import net.pitan76.endercane.EnderCaneMod;
import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;

public class EnderCaneClientMod {
    public static void init() {
        CompatRegistryClient.registerScreen(EnderCaneMod.MOD_ID, EnderCaneMod.ENDER_CANE_TYPE.getOrNull(), EnderCaneScreen::new);
    }
}
