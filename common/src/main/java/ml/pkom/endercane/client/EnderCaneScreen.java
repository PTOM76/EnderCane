package ml.pkom.endercane.client;

import ml.pkom.endercane.EnderCaneMod;
import ml.pkom.endercane.EnderCaneScreenHandler;
import ml.pkom.mcpitanlibarch.api.client.SimpleHandledScreen;
import ml.pkom.mcpitanlibarch.api.client.gui.widget.RedrawableTexturedButtonWidget;
import ml.pkom.mcpitanlibarch.api.network.ClientNetworking;
import ml.pkom.mcpitanlibarch.api.network.PacketByteUtil;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import ml.pkom.mcpitanlibarch.api.util.client.ScreenUtil;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EnderCaneScreen extends SimpleHandledScreen {

    public static Identifier GUI = EnderCaneMod.id("textures/gui/container/ender_cane_gui.png");
    public int selectIndex = -1;
    public int firstIndex = 0;

    public EnderCaneScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    private ButtonWidget ADD_BTN;
    private ButtonWidget REMOVE_BTN;
    private ButtonWidget SET_BTN;
    private RedrawableTexturedButtonWidget POS1_BTN;
    private RedrawableTexturedButtonWidget POS2_BTN;
    private RedrawableTexturedButtonWidget POS3_BTN;
    private RedrawableTexturedButtonWidget POS4_BTN;
    private RedrawableTexturedButtonWidget POS5_BTN;
    private RedrawableTexturedButtonWidget POS6_BTN;
    private RedrawableTexturedButtonWidget POS7_BTN;

    @Override
    public void initOverride() {
        super.initOverride();
        ADD_BTN = addDrawableChild_compatibility(ScreenUtil.createButtonWidget(x + 152, y + 10, 20, 20, TextUtil.translatable("button.endercane.add_point"), (widget) -> {
            // サーバーに送信
            if (handler instanceof EnderCaneScreenHandler) {
                EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) handler;
                PacketByteBuf buf = PacketByteUtil.create();
                buf.writeBlockPos(screenHandler.pos);
                ClientNetworking.send(EnderCaneMod.id("add_point"), buf);
                EnderCaneScreenHandler.addPoint(screenHandler, screenHandler.pos);
            }
        }));
        SET_BTN = addDrawableChild_compatibility(ScreenUtil.createButtonWidget(x + 152, y + 30, 20, 20, TextUtil.translatable("button.endercane.set_point"), (widget) -> {
            // サーバーに送信
            PacketByteBuf buf = PacketByteUtil.create();
            buf.writeInt(selectIndex);
            ClientNetworking.send(EnderCaneMod.id("set_point"), buf);
            if (handler instanceof EnderCaneScreenHandler) {
                EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) handler;
                EnderCaneScreenHandler.setPoint(screenHandler, selectIndex);
            }
        }));
        REMOVE_BTN = addDrawableChild_compatibility(ScreenUtil.createButtonWidget(x + 152, y + 50, 20, 20, TextUtil.translatable("button.endercane.remove_point"), (widget) -> {
            // サーバーに送信
            PacketByteBuf buf = PacketByteUtil.create();
            buf.writeInt(selectIndex);
            ClientNetworking.send(EnderCaneMod.id("remove_point"), buf);
            if (handler instanceof EnderCaneScreenHandler) {
                EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) handler;
                EnderCaneScreenHandler.removePoint(screenHandler, selectIndex);

                if (screenHandler.handStack.hasNbt()) {
                    NbtCompound nbt = screenHandler.handStack.getNbt();

                    NbtList points = new NbtList();
                    if (nbt.contains("Points"))
                        points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);
                    if (points.size() < 1 || selectIndex + 1 > points.size()) {
                        REMOVE_BTN.active = false;
                        SET_BTN.active = false;
                        selectIndex = -1;
                        firstIndex = 0;
                        POS1_BTN.v = 168;POS2_BTN.v = 168;POS3_BTN.v = 168;POS4_BTN.v = 168;
                        POS5_BTN.v = 168;POS6_BTN.v = 168;POS7_BTN.v = 168;
                    }
                }
            }
        }));

        POS1_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 8, 70, 9, 0, 168, 10, GUI, (widget) -> {
            EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) handler;
            NbtCompound nbt = screenHandler.handStack.getNbt();

            NbtList points = new NbtList();
            if (nbt.contains("Points"))
                points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);
            if (firstIndex + 1 > points.size()) return;

            selectIndex = firstIndex;

            REMOVE_BTN.active = true;
            SET_BTN.active = true;


            POS1_BTN.v = 168 + 20;POS2_BTN.v = 168;POS3_BTN.v = 168;POS4_BTN.v = 168;
            POS5_BTN.v = 168;POS6_BTN.v = 168;POS7_BTN.v = 168;
        }));
        POS2_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 17, 70, 9, 0, 168, 10, GUI, (widget) -> {
            EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) handler;
            NbtCompound nbt = screenHandler.handStack.getNbt();

            NbtList points = new NbtList();
            if (nbt.contains("Points"))
                points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);
            if (1 + firstIndex + 1 > points.size()) return;

            selectIndex = 1 + firstIndex;

            REMOVE_BTN.active = true;
            SET_BTN.active = true;

            POS1_BTN.v = 168;POS2_BTN.v = 168 + 20;POS3_BTN.v = 168;POS4_BTN.v = 168;
            POS5_BTN.v = 168;POS6_BTN.v = 168;POS7_BTN.v = 168;
        }));
        POS3_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 26, 70, 9, 0, 168, 10, GUI, (widget) -> {
            EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) handler;
            NbtCompound nbt = screenHandler.handStack.getNbt();

            NbtList points = new NbtList();
            if (nbt.contains("Points"))
                points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);
            if (2 + firstIndex + 1 > points.size()) return;

            selectIndex = 2 + firstIndex;

            REMOVE_BTN.active = true;
            SET_BTN.active = true;

            POS1_BTN.v = 168;POS2_BTN.v = 168;POS3_BTN.v = 168 + 20;POS4_BTN.v = 168;
            POS5_BTN.v = 168;POS6_BTN.v = 168;POS7_BTN.v = 168;
        }));
        POS4_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 35, 70, 9, 0, 168, 10, GUI, (widget) -> {
            EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) handler;
            NbtCompound nbt = screenHandler.handStack.getNbt();

            NbtList points = new NbtList();
            if (nbt.contains("Points"))
                points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);
            if (3 + firstIndex + 1 > points.size()) return;

            selectIndex = 3 + firstIndex;

            REMOVE_BTN.active = true;
            SET_BTN.active = true;

            POS1_BTN.v = 168;POS2_BTN.v = 168;POS3_BTN.v = 168;POS4_BTN.v = 168 + 20;
            POS5_BTN.v = 168;POS6_BTN.v = 168;POS7_BTN.v = 168;
        }));
        POS5_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 44, 70, 9, 0, 168, 10, GUI, (widget) -> {
            EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) handler;
            NbtCompound nbt = screenHandler.handStack.getNbt();

            NbtList points = new NbtList();
            if (nbt.contains("Points"))
                points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);
            if (4 + firstIndex + 1 > points.size()) return;

            selectIndex = 4 + firstIndex;

            REMOVE_BTN.active = true;
            SET_BTN.active = true;

            POS1_BTN.v = 168;POS2_BTN.v = 168;POS3_BTN.v = 168;POS4_BTN.v = 168;
            POS5_BTN.v = 168 + 20;POS6_BTN.v = 168;POS7_BTN.v = 168;
        }));
        POS6_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 53, 70, 9, 0, 168, 10, GUI, (widget) -> {
            EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) handler;
            NbtCompound nbt = screenHandler.handStack.getNbt();

            NbtList points = new NbtList();
            if (nbt.contains("Points"))
                points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);
            if (5 + firstIndex + 1 > points.size()) return;

            selectIndex = 5 + firstIndex;

            REMOVE_BTN.active = true;
            SET_BTN.active = true;

            POS1_BTN.v = 168;POS2_BTN.v = 168;POS3_BTN.v = 168;POS4_BTN.v = 168;
            POS5_BTN.v = 168;POS6_BTN.v = 168 + 20;POS7_BTN.v = 168;
        }));
        POS7_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 62, 70, 9, 0, 168, 10, GUI, (widget) -> {
            EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) handler;
            NbtCompound nbt = screenHandler.handStack.getNbt();

            NbtList points = new NbtList();
            if (nbt.contains("Points"))
                points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);
            if (6 + firstIndex + 1 > points.size()) return;

            selectIndex = 6 + firstIndex;

            REMOVE_BTN.active = true;
            SET_BTN.active = true;

            POS1_BTN.v = 168;POS2_BTN.v = 168;POS3_BTN.v = 168;POS4_BTN.v = 168;
            POS5_BTN.v = 168;POS6_BTN.v = 168;POS7_BTN.v = 168 + 20;
        }));

        REMOVE_BTN.active = false;
        SET_BTN.active = false;
    }

    @Override
    protected void drawForegroundOverride(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForegroundOverride(matrices, mouseX, mouseY);
        if (handler instanceof EnderCaneScreenHandler) {
            EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) handler;
            if (screenHandler.handStack.hasNbt() && screenHandler.handStack.getNbt().contains("Points")) {
                NbtCompound nbt = screenHandler.handStack.getNbt();
                NbtList points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);
                for (int i = 0; i < 7; i++) {
                    if (points.size() >= 1 + firstIndex + i) {
                        NbtCompound pos = points.getCompound(firstIndex + i);
                        textRenderer.draw(matrices, TextUtil.literal("§e" + (firstIndex + i + 1) + ".§r" + pos.getInt("x") + ", " + pos.getInt("y") + ", " + pos.getInt("z")), 80 + 1, 8 + (9 * i) + 1, 0xFFFFFF);
                    }
                }
            }
        }
    }

    @Override
    public void drawBackgroundOverride(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        ScreenUtil.setBackground(GUI);
        callDrawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        EnderCaneScreenHandler screenHandler = (EnderCaneScreenHandler) handler;
        NbtCompound nbt = screenHandler.handStack.getNbt();

        NbtList points = new NbtList();
        if (nbt.contains("Points"))
            points = nbt.getList("Points", NbtElement.COMPOUND_TYPE);

        firstIndex -= Math.round(amount);
        if (firstIndex + 7 > Math.round(points.size())) {
            firstIndex += Math.round(amount);
        }
        if (firstIndex < 0) {
            firstIndex = 0;
        }
        return true;
    }

    @Override
    public void renderOverride(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.callRenderBackground(matrices);
        super.renderOverride(matrices, mouseX, mouseY, delta);
        this.callDrawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
