package mcjty.lib.gui.widgets;

import mcjty.lib.gui.RenderHelper;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.icons.IIcon;
import mcjty.lib.gui.icons.IconManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class IconHolder extends AbstractWidget<IconHolder> {

    private IIcon icon;
    private int border = 0;
    private boolean makeCopy = false;

    public IconHolder(Minecraft mc, Gui gui) {
        super(mc, gui);
    }

    public IIcon getIcon() {
        return icon;
    }

    public IconHolder setIcon(IIcon icon) {
        this.icon = icon;
        return this;
    }

    public int getBorder() {
        return border;
    }

    public IconHolder setBorder(int border) {
        this.border = border;
        return this;
    }

    public boolean isMakeCopy() {
        return makeCopy;
    }

    public IconHolder setMakeCopy(boolean makeCopy) {
        this.makeCopy = makeCopy;
        return this;
    }

    @Override
    public Widget mouseClick(Window window, int x, int y, int button) {
        if (isEnabledAndVisible()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            } else {
                if (icon != null) {
                    IconManager iconManager = window.getWindowManager().getIconManager();
                    Rectangle windowBounds = window.getToplevel().getBounds();
                    iconManager.startDragging(icon, this, x - this.bounds.x, y - this.bounds.y);
                    if (makeCopy) {
                        // @todo
                        icon = null;
                    } else {
                        icon = null;
                    }
                }
            }
//            fireChoiceEvents(choiceList.get(currentChoice));
        }
        return null;
    }


    @Override
    public void draw(Window window, int x, int y) {
        if (!visible) {
            return;
        }
        super.draw(window, x, y);

        int xx = x + bounds.x;
        int yy = y + bounds.y;

        if (border > 0) {
            RenderHelper.drawFlatBox(xx, yy, xx + bounds.width - 1, yy + bounds.height - 1, 0xffffffff, -1);
        }

        if (icon != null) {
            icon.draw(mc, gui, xx+border, yy+border);
        }
    }
}