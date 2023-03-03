package ml.pkom.endercane.client;

import ml.pkom.endercane.EnderCaneMod;
import ml.pkom.mcpitanlibarch.api.client.SimpleHandledScreen;
import ml.pkom.mcpitanlibarch.api.util.client.ScreenUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EnderCaneScreen extends SimpleHandledScreen {

    public static Identifier GUI = EnderCaneMod.id("textures/gui/container/ender_cane_gui.png");

    public EnderCaneScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    public void drawBackgroundOverride(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        ScreenUtil.setBackground(GUI);
        callDrawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void renderOverride(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.callRenderBackground(matrices);
        super.renderOverride(matrices, mouseX, mouseY, delta);
        this.callDrawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
