package mc.recraftors.unruled_api.mixin;

import mc.recraftors.unruled_api.*;
import mc.recraftors.unruled_api.utils.IGameRulesVisitor;
import mc.recraftors.unruled_api.widgets.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net/minecraft/client/gui/screen/world/EditGameRulesScreen$RuleListWidget$1")
public abstract class EditGameRulesScreenAnonymousVisitorMixin implements IGameRulesVisitor {
    @Shadow protected abstract <T extends GameRules.Rule<T>> void createRuleWidget(GameRules.Key<T> key, EditGameRulesScreen.RuleWidgetFactory<T> widgetFactory);

    @Override
    public void unruled_visitFloat(GameRules.Key<FloatRule> key, GameRules.Type<FloatRule> type) {
        this.createRuleWidget(key,
                (name, description, ruleName, rule) -> new FloatRuleWidget(name, description, ruleName, rule, (EditGameRulesScreen) MinecraftClient.getInstance().currentScreen));
        IGameRulesVisitor.super.unruled_visitFloat(key, type);
    }

    @Override
    public void unruled_visitLong(GameRules.Key<LongRule> key, GameRules.Type<LongRule> type) {
        this.createRuleWidget(key,
                (name, description, ruleName, rule) -> new LongRuleWidget(name, description, ruleName, rule, (EditGameRulesScreen) MinecraftClient.getInstance().currentScreen));
        IGameRulesVisitor.super.unruled_visitLong(key, type);
    }

    @Override
    public void unruled_visitDouble(GameRules.Key<DoubleRule> key, GameRules.Type<DoubleRule> type) {
        this.createRuleWidget(key,
                (name, description, ruleName, rule) -> new DoubleRuleWidget(name, description, ruleName, rule, (EditGameRulesScreen) MinecraftClient.getInstance().currentScreen));
        IGameRulesVisitor.super.unruled_visitDouble(key, type);
    }

    @Override
    public <T extends Enum<T>> void unruled_visitEnum(GameRules.Key<EnumRule<T>> key, GameRules.Type<EnumRule<T>> type) {
        this.createRuleWidget(key,
                (name, description, ruleName, rule) -> new EnumRuleWidget<>(name, description, ruleName, rule, (EditGameRulesScreen) MinecraftClient.getInstance().currentScreen));
        IGameRulesVisitor.super.unruled_visitEnum(key, type);
    }

    @Override
    public void unruled_visitString(GameRules.Key<StringRule> key, GameRules.Type<StringRule> type) {
        this.createRuleWidget(key,
                (name, description, ruleName, rule) -> new StringRuleWidget(name, description, ruleName, rule, (EditGameRulesScreen) MinecraftClient.getInstance().currentScreen));
        IGameRulesVisitor.super.unruled_visitString(key, type);
    }

    @Override
    public void unruled_visitText(GameRules.Key<StringRule> key, GameRules.Type<StringRule> type) {
        this.createRuleWidget(key,
                (name, description, ruleName, rule) -> new TextRuleWidget(name, description, ruleName, rule, (EditGameRulesScreen) MinecraftClient.getInstance().currentScreen));
        IGameRulesVisitor.super.unruled_visitText(key, type);
    }
}
