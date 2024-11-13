package mc.recraftors.unruled_api.impl;

import java.util.Optional;

public class BoundedLongRuleValidatorAdapter extends GameruleValidatorAdapter<Long> {
    final long lower;
    final long upper;

    public BoundedLongRuleValidatorAdapter(long lower, long upper) {
        if (lower > upper) throw new IllegalArgumentException("Upper bound cannot be inferior to lower bound");
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public Optional<Long> adapt(Long l) {
        if (l > this.upper) l = this.upper;
        else if (l < this.lower) l = this.lower;
        return Optional.of(l);
    }

    @Override
    public boolean validate(Long l) {
        return this.lower <= l && l <= this.upper;
    }
}
