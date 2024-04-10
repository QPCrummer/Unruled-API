package mc.recraftors.unruled_api.widgets;

import mc.recraftors.unruled_api.rules.StringRule;
import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class TextRuleWidget extends NamedRuleWidget {
    private final ButtonWidget buttonWidget;

    public TextRuleWidget(Text name, List<OrderedText> description, String ruleName, StringRule rule, EditGameRulesScreen screen) {
        super(description, name, screen);
        this.buttonWidget = new ButtonWidget(10, 5, 44, 20, Text.literal("NYI").formatted(Formatting.RED), button -> {});
        this.children.add(this.buttonWidget);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        this.drawName(matrices, y, x);
        this.buttonWidget.x = x + entryWidth - 45;
        this.buttonWidget.y = y;
        this.buttonWidget.render(matrices, mouseX, mouseY, tickDelta);
    }
}
