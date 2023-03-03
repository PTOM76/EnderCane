package ml.pkom.endercane.forge;

import dev.architectury.platform.forge.EventBuses;
import ml.pkom.endercane.EnderCaneMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EnderCaneMod.MOD_ID)
public class EnderCaneForge {
    public EnderCaneForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(EnderCaneMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        EnderCaneMod.init();
    }
}