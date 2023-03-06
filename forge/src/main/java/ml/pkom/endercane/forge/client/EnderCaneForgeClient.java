package ml.pkom.endercane.forge.client;

import ml.pkom.endercane.client.EnderCaneClientMod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class EnderCaneForgeClient {
    public static void clientInit(FMLClientSetupEvent event) {
        EnderCaneClientMod.init();
    }
}
