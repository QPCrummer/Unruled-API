package mc.recraftors.unruled_api.widgets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Multi-line scrollable text block input widget.
 * <p>
 * Inspired by
 * <a href="https://github.com/Tectato/BetterCommandBlockUI/blob/main/src/main/java/bettercommandblockui/main/ui/MultiLineTextFieldWidget.java">
 * Tectato's BetterCommandBlockUI widget</a>
 */
public class MultiLineTextFieldWidget extends TextFieldWidget implements Element {
    private final int visibleChars = 20;
    private int visibleLines = 11;
    private int lineScroll = 0;
    private int horizontalOffset = 0;
    private int maxLineWidth = 30;
    private List<String> lines;

    public MultiLineTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, text);
    }

    /*@Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!visible) return;
        if (super.drawsBackground()) {
            TextFieldWidget.fill(matrices, x - 1, y - 1, x + width + 1, y + height + 1, isFocused() ? -1 : -6250336);
            TextFieldWidget.fill(matrices, x, y, x + width, y + height, -16777216);
        }
        int color = isEditable() ? editableColor : uneditableColor;
    }*/

    public void setScroll(double value) {
        this.lineScroll = (int)Math.max(Math.round((lines.size() - visibleLines) * value), 0);
        refresh();
    }

    public void setHorizontalOffset(double value) {
        this.horizontalOffset = (int)Math.max((double) ((maxLineWidth - visibleChars) * visibleChars), 0);
        refresh();
    }

    void refresh() {}
}
