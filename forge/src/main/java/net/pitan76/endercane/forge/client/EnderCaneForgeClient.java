package net.pitan76.endercane.forge.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.pitan76.endercane.client.EnderCaneClientMod;

public class EnderCaneForgeClient {
    public static void clientInit(FMLClientSetupEvent event) {
        EnderCaneClientMod.init();
    }
}
