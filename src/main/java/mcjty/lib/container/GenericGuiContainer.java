package mcjty.lib.container;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import mcjty.lib.base.ModBase;
import mcjty.lib.entity.GenericTileEntity;
import mcjty.lib.gui.GuiSideWindow;
import mcjty.lib.gui.Window;
import mcjty.lib.network.Argument;
import mcjty.lib.network.PacketServerCommand;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.List;

public abstract class GenericGuiContainer<T extends GenericTileEntity> extends GuiContainer {

    protected ModBase modBase;
    protected SimpleNetworkWrapper network;

    protected Window window;
    protected final T tileEntity;

    private GuiSideWindow sideWindow;

    public GenericGuiContainer(ModBase mod, SimpleNetworkWrapper network, T tileEntity, Container container, int manual, String manualNode) {
        super(container);
        this.modBase = mod;
        this.network = network;
        this.tileEntity = tileEntity;
        sideWindow = new GuiSideWindow(manual, manualNode);
    }

    @Override
    public void initGui() {
        super.initGui();
        sideWindow.initGui(modBase, network, mc, this, guiLeft, guiTop, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int i, int i2) {
        List<String> tooltips = window.getTooltips();
        if (tooltips != null) {
            int x = Mouse.getEventX() * width / mc.displayWidth;
            int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;
            drawHoveringText(tooltips, x - guiLeft, y - guiTop, mc.fontRenderer);
        }

        tooltips = sideWindow.getWindow().getTooltips();
        if (tooltips != null) {
            int x = Mouse.getEventX() * width / mc.displayWidth;
            int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;
            drawHoveringText(tooltips, x - guiLeft, y - guiTop, mc.fontRenderer);
        }
        RenderHelper.enableGUIStandardItemLighting();
    }

    protected void drawWindow() {
        window.draw();
        sideWindow.getWindow().draw();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
        window.mouseClicked(x, y, button);
        sideWindow.getWindow().mouseClicked(x, y, button);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        window.handleMouseInput();
        sideWindow.getWindow().handleMouseInput();
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int button) {
        super.mouseMovedOrUp(x, y, button);
        window.mouseMovedOrUp(x, y, button);
        sideWindow.getWindow().mouseMovedOrUp(x, y, button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (!window.keyTyped(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        }
    }

    protected void sendServerCommand(SimpleNetworkWrapper network, String command, Argument... arguments) {
        network.sendToServer(new PacketServerCommand(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,
                command, arguments));
    }
}
