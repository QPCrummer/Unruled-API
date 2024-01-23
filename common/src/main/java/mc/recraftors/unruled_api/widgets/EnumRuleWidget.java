package mc.recraftors.unruled_api.widgets;

import mc.recraftors.unruled_api.EnumRule;
import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

public class EnumRuleWidget <T extends Enum<T>> extends NamedRuleWidget {
    private final CyclingButtonWidget<T> valuesWidget;

    public EnumRuleWidget(Text name, List<OrderedText> description, String ruleName, EnumRule<T> rule, EditGameRulesScreen screen) {
        super(description, name, screen);
        this.valuesWidget = CyclingButtonWidget.<T>builder(t -> Text.of((t).name()))
                .values(rule.values())
                .omitKeyText()
                .narration(button -> button.getGenericNarrationMessage().append("\n").append(ruleName))
                .build(10, 5, 44, 20, name, ((button, value) -> rule.set(value, null)));
        this.children.add(valuesWidget);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        this.drawName(matrices, y, x);
        this.valuesWidget.x = x + entryWidth - 45;
        this.valuesWidget.y = y;
        this.valuesWidget.render(matrices, mouseX, mouseY, tickDelta);
    }
}
