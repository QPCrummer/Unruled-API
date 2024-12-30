package mc.recraftors.unruled_api.impl;

import java.util.Optional;

public class BoundedIntRuleValidatorAdapter extends GameruleValidatorAdapter<Integer> {
    final int lower;
    final int upper;

    public BoundedIntRuleValidatorAdapter(int lower, int upper) {
        if (lower > upper) throw new IllegalArgumentException("Upper bound cannot be inferior to lower bound");
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public Optional<Integer> adapt(Integer integer) {
        return Optional.of(Math.min(upper, Math.max(lower, integer)));
    }

    @Override
    public boolean validate(Integer integer) {
        return lower <= integer && upper >= integer;
    }
}
