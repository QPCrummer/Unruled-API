package mc.recraftors.unruled_api.impl;

import java.util.Optional;

public class BoundedDoubleRuleValidatorAdapter extends GameruleValidatorAdapter<Double> {
    final double lower;
    final double upper;

    public BoundedDoubleRuleValidatorAdapter(double lower, double upper) {
        if (lower > upper) throw new IllegalArgumentException("Upper bound cannot be inferior to lower bound");
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public Optional<Double> adapt(Double d) {
        if (d < this.lower) d = this.lower;
        else if (d > this.upper) d = this.upper;
        return Optional.of(d);
    }

    @Override
    public boolean validate(Double d) {
        return this.lower <= d && d <= this.upper;
    }
}
