package net.pitan76.endercane.client;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.pitan76.endercane.EnderCane;
import net.pitan76.endercane.EnderCaneScreenHandler;
import net.pitan76.mcpitanlib.api.client.gui.screen.CompatInventoryScreen;
import net.pitan76.mcpitanlib.api.client.gui.widget.RedrawableTexturedButtonWidget;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.DrawForegroundArgs;
import net.pitan76.mcpitanlib.api.network.v2.ClientNetworking;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;

import static net.pitan76.endercane.EnderCaneMod._id;

public class EnderCaneScreen extends CompatInventoryScreen<EnderCaneScreenHandler> {

    public static CompatIdentifier GUI = _id("textures/gui/container/ender_cane_gui.png");
    public static CompatIdentifier GUI_INFINITY = _id("textures/gui/container/ender_cane_gui_infinity.png");
    public int selectIndex = -1;
    public int firstIndex = 0;
    public CompatIdentifier CACHE_TEXTURE = null;

    public EnderCaneScreen(EnderCaneScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    public void cacheTexture() {
        EnderCaneScreenHandler screenHandler = this.handler;
        ItemStack handStack = screenHandler.handStack;
        CACHE_TEXTURE = (ItemStackUtil.isEmpty(handStack) || ((EnderCane) handStack.getItem()).getMaxPearlAmount() != -1 ? GUI : GUI_INFINITY);
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
        cacheTexture();

        ADD_BTN = addDrawableChild_compatibility(ScreenUtil.createButtonWidget(x + 152, y + 10, 20, 20, TextUtil.translatable("button.endercane.add_point"), (widget) -> {
            // サーバーに送信
            if (handler instanceof EnderCaneScreenHandler) {
                EnderCaneScreenHandler screenHandler = this.handler;
                PacketByteBuf buf = PacketByteUtil.create();
                PacketByteUtil.writeBlockPos(buf, screenHandler.pos);
                ClientNetworking.send(_id("add_point"), buf);
                EnderCaneScreenHandler.addPoint(screenHandler, screenHandler.pos);
            }
        }));
        SET_BTN = addDrawableChild_compatibility(ScreenUtil.createButtonWidget(x + 152, y + 30, 20, 20, TextUtil.translatable("button.endercane.set_point"), (widget) -> {
            // サーバーに送信
            PacketByteBuf buf = PacketByteUtil.create();
            PacketByteUtil.writeInt(buf, selectIndex);
            ClientNetworking.send(_id("set_point"), buf);
            if (handler instanceof EnderCaneScreenHandler) {
                EnderCaneScreenHandler screenHandler = this.handler;
                EnderCaneScreenHandler.setPoint(screenHandler, selectIndex);
            }
        }));
        REMOVE_BTN = addDrawableChild_compatibility(ScreenUtil.createButtonWidget(x + 152, y + 50, 20, 20, TextUtil.translatable("button.endercane.remove_point"), (widget) -> {
            // サーバーに送信
            PacketByteBuf buf = PacketByteUtil.create();
            PacketByteUtil.writeInt(buf, selectIndex);
            ClientNetworking.send(_id("remove_point"), buf);
            if (!(handler instanceof EnderCaneScreenHandler)) return;

            EnderCaneScreenHandler screenHandler = this.handler;
            EnderCaneScreenHandler.removePoint(screenHandler, selectIndex);

            if (!CustomDataUtil.hasNbt(screenHandler.handStack)) return;

            NbtCompound nbt = CustomDataUtil.getNbt(screenHandler.handStack);
            NbtList points = NbtUtil.createNbtList();
            if (NbtUtil.has(nbt, "Points"))
                points = NbtUtil.getNbtCompoundList(nbt, "Points");
            if (points.isEmpty() || selectIndex + 1 > points.size()) {
                REMOVE_BTN.active = false;
                SET_BTN.active = false;
                selectIndex = -1;
                firstIndex = 0;
                POS1_BTN.v = 168;POS2_BTN.v = 168;POS3_BTN.v = 168;POS4_BTN.v = 168;
                POS5_BTN.v = 168;POS6_BTN.v = 168;POS7_BTN.v = 168;
            }
        }));

        POS1_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 8, 70, 9, 0, 168, 10, GUI.toMinecraft(), (widget) -> {
            EnderCaneScreenHandler screenHandler = this.handler;
            NbtCompound nbt = CustomDataUtil.getNbt(screenHandler.handStack);

            NbtList points = NbtUtil.createNbtList();
            if (NbtUtil.has(nbt, "Points"))
                points = NbtUtil.getNbtCompoundList(nbt, "Points");
            if (firstIndex + 1 > points.size()) return;

            selectIndex = firstIndex;

            REMOVE_BTN.active = true;
            SET_BTN.active = true;

            POS1_BTN.v = 168 + 20;POS2_BTN.v = 168;POS3_BTN.v = 168;POS4_BTN.v = 168;
            POS5_BTN.v = 168;POS6_BTN.v = 168;POS7_BTN.v = 168;
        }));
        POS2_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 17, 70, 9, 0, 168, 10, GUI.toMinecraft(), (widget) -> {
            EnderCaneScreenHandler screenHandler = this.handler;
            NbtCompound nbt = CustomDataUtil.getNbt(screenHandler.handStack);

            NbtList points = NbtUtil.createNbtList();
            if (NbtUtil.has(nbt, "Points"))
                points = NbtUtil.getNbtCompoundList(nbt, "Points");
            if (1 + firstIndex + 1 > points.size()) return;

            selectIndex = 1 + firstIndex;

            REMOVE_BTN.active = true;
            SET_BTN.active = true;

            POS1_BTN.v = 168;POS2_BTN.v = 168 + 20;POS3_BTN.v = 168;POS4_BTN.v = 168;
            POS5_BTN.v = 168;POS6_BTN.v = 168;POS7_BTN.v = 168;
        }));
        POS3_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 26, 70, 9, 0, 168, 10, GUI.toMinecraft(), (widget) -> {
            EnderCaneScreenHandler screenHandler = this.handler;
            NbtCompound nbt = CustomDataUtil.getNbt(screenHandler.handStack);

            NbtList points = NbtUtil.createNbtList();
            if (NbtUtil.has(nbt, "Points"))
                points = NbtUtil.getNbtCompoundList(nbt, "Points");
            if (2 + firstIndex + 1 > points.size()) return;

            selectIndex = 2 + firstIndex;

            REMOVE_BTN.active = true;
            SET_BTN.active = true;

            POS1_BTN.v = 168;POS2_BTN.v = 168;POS3_BTN.v = 168 + 20;POS4_BTN.v = 168;
            POS5_BTN.v = 168;POS6_BTN.v = 168;POS7_BTN.v = 168;
        }));
        POS4_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 35, 70, 9, 0, 168, 10, GUI.toMinecraft(), (widget) -> {
            EnderCaneScreenHandler screenHandler = this.handler;
            NbtCompound nbt = CustomDataUtil.getNbt(screenHandler.handStack);

            NbtList points = NbtUtil.createNbtList();
            if (NbtUtil.has(nbt, "Points"))
                points = NbtUtil.getNbtCompoundList(nbt, "Points");
            if (3 + firstIndex + 1 > points.size()) return;

            selectIndex = 3 + firstIndex;

            REMOVE_BTN.active = true;
            SET_BTN.active = true;

            POS1_BTN.v = 168;POS2_BTN.v = 168;POS3_BTN.v = 168;POS4_BTN.v = 168 + 20;
            POS5_BTN.v = 168;POS6_BTN.v = 168;POS7_BTN.v = 168;
        }));
        POS5_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 44, 70, 9, 0, 168, 10, GUI.toMinecraft(), (widget) -> {
            EnderCaneScreenHandler screenHandler = this.handler;
            NbtCompound nbt = CustomDataUtil.getNbt(screenHandler.handStack);

            NbtList points = NbtUtil.createNbtList();
            if (NbtUtil.has(nbt, "Points"))
                points = NbtUtil.getNbtCompoundList(nbt, "Points");
            if (4 + firstIndex + 1 > points.size()) return;

            selectIndex = 4 + firstIndex;

            REMOVE_BTN.active = true;
            SET_BTN.active = true;

            POS1_BTN.v = 168;POS2_BTN.v = 168;POS3_BTN.v = 168;POS4_BTN.v = 168;
            POS5_BTN.v = 168 + 20;POS6_BTN.v = 168;POS7_BTN.v = 168;
        }));
        POS6_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 53, 70, 9, 0, 168, 10, GUI.toMinecraft(), (widget) -> {
            EnderCaneScreenHandler screenHandler = this.handler;
            NbtCompound nbt = CustomDataUtil.getNbt(screenHandler.handStack);

            NbtList points = NbtUtil.createNbtList();
            if (NbtUtil.has(nbt, "Points"))
                points = NbtUtil.getNbtCompoundList(nbt, "Points");
            if (5 + firstIndex + 1 > points.size()) return;

            selectIndex = 5 + firstIndex;

            REMOVE_BTN.active = true;
            SET_BTN.active = true;

            POS1_BTN.v = 168;POS2_BTN.v = 168;POS3_BTN.v = 168;POS4_BTN.v = 168;
            POS5_BTN.v = 168;POS6_BTN.v = 168 + 20;POS7_BTN.v = 168;
        }));
        POS7_BTN = addDrawableChild_compatibility(ScreenUtil.createRedrawableTexturedButtonWidget(x + 80, y + 62, 70, 9, 0, 168, 10, GUI.toMinecraft(), (widget) -> {
            EnderCaneScreenHandler screenHandler = this.handler;
            NbtCompound nbt = CustomDataUtil.getNbt(screenHandler.handStack);

            NbtList points = NbtUtil.createNbtList();
            if (NbtUtil.has(nbt, "Points"))
                points = NbtUtil.getNbtCompoundList(nbt, "Points");
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
    protected void drawForegroundOverride(DrawForegroundArgs args) {
        super.drawForegroundOverride(args);
        if (!(handler instanceof EnderCaneScreenHandler)) return;

        EnderCaneScreenHandler screenHandler = this.handler;
        if (CustomDataUtil.hasNbt(screenHandler.handStack) && CustomDataUtil.has(screenHandler.handStack, "Points")) {
            NbtCompound nbt = CustomDataUtil.getNbt(screenHandler.handStack);
            NbtList points = NbtUtil.getNbtCompoundList(nbt, "Points");
            for (int i = 0; i < 7; i++) {
                if (points.size() >= 1 + firstIndex + i) {
                    NbtCompound posNbt = points.getCompound(firstIndex + i);
                    ScreenUtil.RendererUtil.drawText(textRenderer, args.drawObjectDM, TextUtil.literal("§e" + (firstIndex + i + 1) + ".§r" + NbtUtil.getInt(posNbt, "x") + ", " + NbtUtil.getInt(posNbt, "y") + ", " + NbtUtil.getInt(posNbt, "z")), 80 + 1, 8 + (9 * i) + 1, 0xFFFFFF);
                }
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        EnderCaneScreenHandler screenHandler = this.handler;
        NbtCompound nbt = CustomDataUtil.getNbt(screenHandler.handStack);

        NbtList points = NbtUtil.createNbtList();
        if (NbtUtil.has(nbt, "Points"))
            points = NbtUtil.getNbtCompoundList(nbt, "Points");

        firstIndex -= (int) Math.round(amount);
        if (firstIndex + 7 > points.size()) {
            firstIndex += (int) Math.round(amount);
        }
        if (firstIndex < 0) {
            firstIndex = 0;
        }
        return true;
    }

    @Override
    public CompatIdentifier getCompatTexture() {
        if (CACHE_TEXTURE == null)
            cacheTexture();
        return CACHE_TEXTURE;
    }
}
