package mc.recraftors.unruled_api.impl;

import java.util.Optional;

public class BoundedFloatRuleValidatorAdapter extends GameruleValidatorAdapter<Float> {
    final float lower;
    final float upper
            ;

    public BoundedFloatRuleValidatorAdapter(float lower, float upper) {
        if (lower > upper) throw new IllegalArgumentException("Upper bound cannot be inferior to lower bound");
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public Optional<Float> adapt(Float f) {
        if (f < this.lower) f = this.lower;
        else if (f > this.upper) f = this.upper;
        return Optional.of(f);
    }

    @Override
    public boolean validate(Float f) {
        return this.lower <= f && f <= this.upper;
    }
}
