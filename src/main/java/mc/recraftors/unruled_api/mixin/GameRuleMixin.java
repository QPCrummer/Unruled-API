package mc.recraftors.unruled_api.mixin;

import mc.recraftors.unruled_api.utils.GameruleAccessor;
import mc.recraftors.unruled_api.utils.IGameruleAdapter;
import mc.recraftors.unruled_api.utils.IGameruleValidator;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(GameRules.Rule.class)
public abstract class GameRuleMixin<T> implements GameruleAccessor<T> {
    @Unique
    IGameruleAdapter<T> unruled_default_adapter = Optional::ofNullable;

    @Unique
    IGameruleValidator<T> unruled_default_validator = e -> true;

    @Override
    public void unruled_setAdapter(IGameruleAdapter<T> adapter) {
        this.unruled_default_adapter = adapter;
    }

    @Override
    public void unruled_setValidator(IGameruleValidator<T> validator) {
        this.unruled_default_validator = validator;
    }

    @Override
    public IGameruleAdapter<T> unruled_getAdapter() {
        return this.unruled_default_adapter;
    }

    @Override
    public IGameruleValidator<T> unruled_getValidator() {
        return this.unruled_default_validator;
    }
}
