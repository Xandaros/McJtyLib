package mcjty.lib.gui;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import mcjty.lib.base.ModBase;
import mcjty.lib.gui.events.ButtonEvent;
import mcjty.lib.gui.layout.PositionalLayout;
import mcjty.lib.gui.widgets.Button;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.Widget;
import mcjty.lib.network.PacketSetGuiStyle;
import mcjty.lib.preferences.PlayerPreferencesProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

public class GuiSideWindow {
    protected GuiStyle style;

    protected Window sideWindow;
    private Button guiButton;
    private Button helpButton;
    private int sideLeft;
    private int sideTop;

    private int manual;
    private String manualNode;

    public GuiSideWindow(int manual, String manualNode) {
        this.manual = manual;
        this.manualNode = manualNode;
    }

    public void initGui(final ModBase modBase, final SimpleNetworkWrapper network, final Minecraft mc, GuiScreen gui, int guiLeft, int guiTop, int xSize, int ySize) {
        style = PlayerPreferencesProperties.getProperties(mc.thePlayer).getPreferencesProperties().getStyle();

        helpButton = new Button(mc, gui).setText("?").setLayoutHint(new PositionalLayout.PositionalHint(1, 1, 16, 16)).
                setTooltips("Open manual").
                addButtonEvent(new ButtonEvent() {
                    @Override
                    public void buttonClicked(Widget parent) {
                        help(modBase, mc);
                    }
                });
        guiButton = new Button(mc, gui).setText("s").setLayoutHint(new PositionalLayout.PositionalHint(1, 19, 16, 16)).
                addButtonEvent(new ButtonEvent() {
                    @Override
                    public void buttonClicked(Widget parent) {
                        changeStyle(network);
                    }
                });
        setStyleTooltip();
        Panel sidePanel = new Panel(mc, gui).setLayout(new PositionalLayout()).addChild(guiButton).addChild(helpButton);
        sideLeft = guiLeft + xSize;
        sideTop = guiTop + (ySize - 20) / 2 - 8;
        sidePanel.setBounds(new Rectangle(sideLeft, sideTop, 20, 40));
        sideWindow = new Window(gui, sidePanel);
    }

    private void help(ModBase modBase, Minecraft mc) {
        EntityPlayer player = mc.thePlayer;
        modBase.openManual(player, manual, manualNode);
    }

    private void setStyleTooltip() {
        guiButton.setTooltips("Gui style:", style.getStyle());
    }

    private void changeStyle(SimpleNetworkWrapper network) {
        int next = style.ordinal() + 1;
        if (next >= GuiStyle.values().length) {
            next = 0;
        }
        style = GuiStyle.values()[next];
        network.sendToServer(new PacketSetGuiStyle(style.getStyle()));

        setStyleTooltip();
    }

    public Window getWindow() {
        return sideWindow;
    }
}
