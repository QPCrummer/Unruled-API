package mc.recraftors.unruled_api.impl;

import java.util.Optional;

public class EvenIntRuleValidatorAdapter extends GameruleValidatorAdapter<Integer> {
    final RoundingBehaviour rounding;

    public EvenIntRuleValidatorAdapter(RoundingBehaviour rounding) {
        this.rounding = rounding;
    }

    @Override
    public Optional<Integer> adapt(Integer i) {
        return switch (this.rounding) {
            case NONE -> Optional.empty();
            case FLOOR -> {
                if (i == Integer.MIN_VALUE) yield Optional.empty();
                yield Optional.of(i - 1);
            }
            default -> {
                if (i == Integer.MAX_VALUE) yield Optional.empty();
                yield Optional.of(i + 1);
            }
        };
    }

    @Override
    public boolean validate(Integer i) {
        return (i & 1) == 0;
    }
}
