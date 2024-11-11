package mc.recraftors.unruled_api;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import mc.recraftors.unruled_api.impl.GameruleValidatorAdapter;
import mc.recraftors.unruled_api.rules.*;
import mc.recraftors.unruled_api.utils.GameruleAccessor;
import mc.recraftors.unruled_api.utils.IGameruleAdapter;
import mc.recraftors.unruled_api.utils.IGameruleValidator;
import net.minecraft.command.EntitySelector;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

import static net.minecraft.world.GameRules.*;

/**
 * Main API class for creating gamerules beyond the vanilla scope.
 * <p>
 * Use the {@code create<Type>} static methods in order to create your gamerules.
 * <p>
 * Warning, these methods will <i>create</i> the gamerules, but not register them.
 */
@SuppressWarnings("unused")
public class UnruledApi {
	public static final String MOD_ID = "unruled_api";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static <T, U> BiConsumer<T, U> empty() {
		return (t, u) -> {};
	}

	private static int strLen(String s, int i) {
		return Math.max(i, (int) Math.ceil(s.length() / 8d) * 8);
	}

	/**
	 * Registers the provided gamerule in the specified category and with the specified name,
	 * and returns the registered rule's key.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param type The type of gamerule to register.
	 * @return The new rule registration key.
	 * @param <T> The gamerule's type.
	 */
	public static <T extends Rule<T>> Key<T> register(String name, Category category, Type<T> type) {
		return GameRules.register(name, category, type);
	}

	/**
	 * Creates a vanilla Boolean gamerule with the provided default value and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static Type<BooleanRule> createBoolean(boolean initialValue, BiConsumer<MinecraftServer, BooleanRule> changeCallback) {
		return BooleanRule.create(initialValue, changeCallback);
	}

	/**
	 * Creates a vanilla Boolean gamerule with the provided default value.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_ -> new", pure = true)
	@NotNull public static Type<BooleanRule> createBoolean(boolean initialValue) {
		return BooleanRule.create(initialValue);
	}

	/**
	 * Creates and registers a vanilla Boolean gamerule with the provided default
	 * value and change callback, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _ -> new")
	@NotNull public static Key<BooleanRule> registerBoolean(String name, Category category, boolean initialValue, BiConsumer<MinecraftServer, BooleanRule> changeCallback) {
		return register(name, category, createBoolean(initialValue, changeCallback));
	}

	/**
	 * Creates and registers a vanilla Boolean gamerule with the provided default
	 * value, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _ -> new")
	@NotNull public static Key<BooleanRule> registerBoolean(String name, Category category, boolean initialValue) {
		return register(name, category, createBoolean(initialValue));
	}

	/**
	 * Creates a new Double gamerule, able to hold values from {@link Double#MIN_VALUE} to
	 * {@link Double#MAX_VALUE}, with the provided default value and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's value validator.
	 * @param adapter The new gamerule's invalid value adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _, _ -> new", pure = true)
	@NotNull public static Type<DoubleRule> createDouble(
			double initialValue, BiConsumer<MinecraftServer, DoubleRule> changeCallback,
			IGameruleValidator<Double> validator, IGameruleAdapter<Double> adapter
	) {
		return DoubleRule.create(initialValue, changeCallback, validator, adapter);
	}

	/**
	 * Creates a new Double gamerule, able to hold values from {@link Double#MIN_VALUE} to
	 * {@link Double#MAX_VALUE}, with the provided default value and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static Type<DoubleRule> createDouble(
			double initialValue, BiConsumer<MinecraftServer, DoubleRule> changeCallback
	) {
		return DoubleRule.create(initialValue, changeCallback);
	}

	/**
	 * Creates a new Double gamerule, able to hold values from {@link Double#MIN_VALUE} to
	 * {@link Double#MAX_VALUE}, with the provided default value.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's value validator.
	 * @param adapter The new gamerule's invalid value adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _ -> new", pure = true)
	@NotNull public static Type<DoubleRule> createDouble(
			double initialValue, IGameruleValidator<Double> validator, IGameruleAdapter<Double> adapter
	) {
		return DoubleRule.create(initialValue, validator, adapter);
	}

	/**
	 * Creates a new Double gamerule, able to hold values from {@link Double#MIN_VALUE} to
	 * {@link Double#MAX_VALUE}, with the provided default value.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_ -> new", pure = true)
	@NotNull public static Type<DoubleRule> createDouble(double initialValue) {
		return DoubleRule.create(initialValue);
	}

	/**
	 * Creates and registers a new Double gamerule, able to hold value from
	 * {@link Double#MIN_VALUE} to {@link Double#MAX_VALUE}, with the provided
	 * default value and change callback, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's value validator.
	 * @param adapter The new gamerule's invalid value adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _, _ -> new")
	@NotNull public static Key<DoubleRule> registerDouble(
			String name, Category category, double initialValue, BiConsumer<MinecraftServer, DoubleRule> changeCallback,
			IGameruleValidator<Double> validator, IGameruleAdapter<Double> adapter
	) {
		return register(name, category, createDouble(initialValue, changeCallback, validator, adapter));
	}

	/**
	 * Creates and registers a new Double gamerule, able to hold value from
	 * {@link Double#MIN_VALUE} to {@link Double#MAX_VALUE}, with the provided
	 * default value and change callback, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _ -> new")
	@NotNull public static Key<DoubleRule> registerDouble(
			String name, Category category, double initialValue, BiConsumer<MinecraftServer, DoubleRule> changeCallback
	) {
		return register(name, category, createDouble(initialValue, changeCallback));
	}

	/**
	 * Creates and registers a new Double gamerule, able to hold value from
	 * {@link Double#MIN_VALUE} to {@link Double#MAX_VALUE}, with the provided
	 * default value, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's value validator.
	 * @param adapter The new gamerule's invalid value adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _ -> new")
	@NotNull public static Key<DoubleRule> registerDouble(
			String name, Category category, double initialValue, IGameruleValidator<Double> validator,
			IGameruleAdapter<Double> adapter
	) {
		return register(name, category, createDouble(initialValue));
	}

	/**
	 * Creates and registers a new Double gamerule, able to hold value from
	 * {@link Double#MIN_VALUE} to {@link Double#MAX_VALUE}, with the provided
	 * default value, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _ -> new")
	@NotNull public static Key<DoubleRule> registerDouble(String name, Category category, double initialValue) {
		return register(name, category, createDouble(initialValue));
	}

	/**
	 * Creates a new Enum gamerule of the specified class, with the provided default value
	 * and change callback.
	 * @param tClass The new gamerule's enum class.
	 * @param initialValue The new gamerule's default value. Must be of the provided enum class.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's value validator.
	 * @param adapter The new gamerule's invalid value adapter.
	 * @return The newly created gamerule's reference.
	 * @param <T> The new gamerule's enum type.
	 */
	@Contract(value = "_, _, _, _, _ -> new", pure = true)
	@NotNull public static <T extends Enum<T>> Type<EnumRule<T>> createEnum(
			Class<T> tClass, T initialValue, BiConsumer<MinecraftServer, EnumRule<T>> changeCallback,
			IGameruleValidator<T> validator, IGameruleAdapter<T> adapter
	) {
		return EnumRule.create(tClass, initialValue, changeCallback, validator, adapter);
	}

	/**
	 * Creates a new Enum gamerule of the specified class, with the provided default value
	 * and change callback.
	 * @param tClass The new gamerule's enum class.
	 * @param initialValue The new gamerule's default value. Must be of the provided enum class.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 * @param <T> The new gamerule's enum type.
	 */
	@Contract(value = "_, _, _ -> new", pure = true)
	@NotNull public static <T extends Enum<T>> Type<EnumRule<T>> createEnum(
			Class<T> tClass, T initialValue, BiConsumer<MinecraftServer, EnumRule<T>> changeCallback
	) {
		return EnumRule.create(tClass, initialValue, changeCallback);
	}

	/**
	 * Creates a new Enum gamerule of the specified class, with the provided default value.
	 * @param tClass The new gamerule's enum class.
	 * @param initialValue The new gamerule's default value. Must be of the provided enum class.
	 * @param validator The new gamerule's value validator.
	 * @param adapter The new gamerule's invalid value adapter.
	 * @return The newly created gamerule's reference.
	 * @param <T> The new gamerule's enum type.
	 */
	@Contract(value = "_, _ , _, _-> new", pure = true)
	@NotNull public static <T extends Enum<T>> Type<EnumRule<T>> createEnum(
			Class<T> tClass, T initialValue, IGameruleValidator<T> validator, IGameruleAdapter<T> adapter
	) {
		return EnumRule.create(tClass, initialValue, validator, adapter);
	}

	/**
	 * Creates a new Enum gamerule of the specified class, with the provided default value.
	 * @param tClass The new gamerule's enum class.
	 * @param initialValue The new gamerule's default value. Must be of the provided enum class.
	 * @return The newly created gamerule's reference.
	 * @param <T> The new gamerule's enum type.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static <T extends Enum<T>> Type<EnumRule<T>> createEnum(Class<T> tClass, T initialValue) {
		return EnumRule.create(tClass, initialValue);
	}

	/**
	 * Creates and registers a new Enum gamerule of the specified class,
	 * with the provided default value, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param tClass The new gamerule's enum class.
	 * @param initialValue The new gamerule's default value. Must be of the provided enum class.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's value validator.
	 * @param adapter The new gamerule's invalid value adapter.
	 * @return The new rule registration key.
	 * @param <T> The new gamerule's enum type.
	 */
	@Contract("_, _, _, _, _, _, _ -> new")
	@NotNull public static <T extends Enum<T>> Key<EnumRule<T>> registerEnum(
			String name, Category category, Class<T> tClass, T initialValue,
			BiConsumer<MinecraftServer, EnumRule<T>> changeCallback, IGameruleValidator<T> validator,
			IGameruleAdapter<T> adapter
	) {
		return register(name, category, createEnum(tClass, initialValue, changeCallback, validator, adapter));
	}

	/**
	 * Creates and registers a new Enum gamerule of the specified class,
	 * with the provided default value, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param tClass The new gamerule's enum class.
	 * @param initialValue The new gamerule's default value. Must be of the provided enum class.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The new rule registration key.
	 * @param <T> The new gamerule's enum type.
	 */
	@Contract("_, _, _, _, _ -> new")
	@NotNull public static <T extends Enum<T>> Key<EnumRule<T>> registerEnum(
			String name, Category category, Class<T> tClass, T initialValue,
			BiConsumer<MinecraftServer, EnumRule<T>> changeCallback
	) {
		return register(name, category, createEnum(tClass, initialValue, changeCallback));
	}

	/**
	 * Creates and registers a new Enum gamerule of the specified class,
	 * with the provided default value, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param tClass The new gamerule's enum class.
	 * @param initialValue The new gamerule's default value. Must be of the provided enum class.
	 * @param validator The new gamerule's value validator.
	 * @param adapter The new gamerule's invalid value adapter.
	 * @return The new rule registration key.
	 * @param <T> The new gamerule's enum type.
	 */
	@Contract("_, _, _, _, _, _ -> new")
	@NotNull public static <T extends Enum<T>> Key<EnumRule<T>> registerEnum(
			String name, Category category, Class<T> tClass, T initialValue, IGameruleValidator<T> validator,
			IGameruleAdapter<T> adapter
	) {
		return register(name, category, createEnum(tClass, initialValue, validator, adapter));
	}

	/**
	 * Creates and registers a new Enum gamerule of the specified class,
	 * with the provided default value, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param tClass The new gamerule's enum class.
	 * @param initialValue The new gamerule's default value. Must be of the provided enum class.
	 * @return The new rule registration key.
	 * @param <T> The new gamerule's enum type.
	 */
	@Contract("_, _, _, _ -> new")
	@NotNull public static <T extends Enum<T>> Key<EnumRule<T>> registerEnum(
			String name, Category category, Class<T> tClass, T initialValue
	) {
		return register(name, category, createEnum(tClass, initialValue));
	}

	/**
	 * Creates a new Float gamerule, able to hold values from {@link Float#MIN_VALUE} to
	 * {@link Float#MAX_VALUE}, with the provided default value and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's value validator.
	 * @param adapter The new gamerule's invalid value adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _, _ -> new", pure = true)
	@NotNull public static Type<FloatRule> createFloat(
			float initialValue, BiConsumer<MinecraftServer, FloatRule> changeCallback,
			IGameruleValidator<Float> validator, IGameruleAdapter<Float> adapter
	) {
		return FloatRule.create(initialValue, changeCallback, validator, adapter);
	}

	/**
	 * Creates a new Float gamerule, able to hold values from {@link Float#MIN_VALUE} to
	 * {@link Float#MAX_VALUE}, with the provided default value and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static Type<FloatRule> createFloat(
			float initialValue, BiConsumer<MinecraftServer, FloatRule> changeCallback
	) {
		return FloatRule.create(initialValue, changeCallback);
	}

	/**
	 * Creates a new Float gamerule, able to hold values from {@link Float#MIN_VALUE} to
	 * {@link Float#MAX_VALUE}, with the provided default value.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's value validator.
	 * @param adapter The new gamerule's invalid value adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _ -> new", pure = true)
	@NotNull public static Type<FloatRule> createFloat(
			float initialValue, IGameruleValidator<Float> validator, IGameruleAdapter<Float> adapter
	) {
		return FloatRule.create(initialValue);
	}

	/**
	 * Creates a new Float gamerule, able to hold values from {@link Float#MIN_VALUE} to
	 * {@link Float#MAX_VALUE}, with the provided default value.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_ -> new", pure = true)
	@NotNull public static Type<FloatRule> createFloat(float initialValue) {
		return FloatRule.create(initialValue);
	}

	/**
	 * Creates and register a new Float gamerule, able to hold values from
	 * {@link Float#MIN_VALUE} to {@link Float#MAX_VALUE}, with the provided
	 * default value and change callback, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's value validator.
	 * @param adapter The new gamerule's invalid value adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _, _ -> new")
	@NotNull public static Key<FloatRule> registerFloat(
			String name, Category category, float initialValue, BiConsumer<MinecraftServer, FloatRule> changeCallback,
			IGameruleValidator<Float> validator, IGameruleAdapter<Float> adapter
	) {
		return register(name, category, createFloat(initialValue, changeCallback, validator, adapter));
	}

	/**
	 * Creates and register a new Float gamerule, able to hold values from
	 * {@link Float#MIN_VALUE} to {@link Float#MAX_VALUE}, with the provided
	 * default value and change callback, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _ -> new")
	@NotNull public static Key<FloatRule> registerFloat(
			String name, Category category, float initialValue, BiConsumer<MinecraftServer, FloatRule> changeCallback
	) {
		return register(name, category, createFloat(initialValue, changeCallback));
	}

	/**
	 * Creates and register a new Float gamerule, able to hold values from
	 * {@link Float#MIN_VALUE} to {@link Float#MAX_VALUE}, with the provided
	 * default value, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's value validator.
	 * @param adapter The new gamerule's invalid value adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _ -> new")
	@NotNull public static Key<FloatRule> registerFloat(
			String name, Category category, float initialValue, IGameruleValidator<Float> validator,
			IGameruleAdapter<Float> adapter
	) {
		return register(name, category, createFloat(initialValue, validator, adapter));
	}

	/**
	 * Creates and register a new Float gamerule, able to hold values from
	 * {@link Float#MIN_VALUE} to {@link Float#MAX_VALUE}, with the provided
	 * default value, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _ -> new")
	@NotNull public static Key<FloatRule> registerFloat(String name, Category category, float initialValue) {
		return register(name, category, createFloat(initialValue));
	}

	/**
	 * Creates a vanilla integer gamerule with the provided default value
	 * and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 * @apiNote Better use {@link #register(String, Category, Type)} directly.
	 */
	@SuppressWarnings({"unchecked", "DataFlowIssue"})
    @Contract(value = "_, _, _, _ -> new", pure = true)
	@NotNull public static Type<IntRule> createInt(
			int initialValue, BiConsumer<MinecraftServer, IntRule> changeCallback,
			IGameruleValidator<Integer> validator, IGameruleAdapter<Integer> adapter) {
		return new Type<>(IntegerArgumentType::integer, type -> {
			IntRule rule = new IntRule(type, initialValue);
			((GameruleAccessor<Integer>)rule).unruled_setValidator(validator);
			((GameruleAccessor<Integer>)rule).unruled_setAdapter(adapter);
			return rule;
		}, changeCallback, Visitor::visitInt);
	}

	/**
	 * Creates a vanilla integer gamerule with the provided default value
	 * and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 * @apiNote Better use {@link #register(String, Category, Type)} directly.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static Type<IntRule> createInt(int initialValue, BiConsumer<MinecraftServer, IntRule> changeCallback) {
		return IntRule.create(initialValue, changeCallback);
	}

	/**
	 * Creates a vanilla integer gamerule with the provided default value.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _ -> new", pure = true)
	@NotNull public static Type<IntRule> createInt(
			int initialValue, IGameruleValidator<Integer> validator, IGameruleAdapter<Integer> adapter
	) {
		return createInt(initialValue, empty(), validator, adapter);
	}

	/**
	 * Creates a vanilla integer gamerule with the provided default value.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_ -> new", pure = true)
	@NotNull public static Type<IntRule> createInt(int initialValue) {
		return IntRule.create(initialValue);
	}

	/**
	 * Creates and registers a new vanilla integer gamerule with the
	 * provided default value and change callback, at the specified
	 * name and in the specified category, with the supplied value
	 * validator and adapter.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validatorAdapter The new gamerule's validator and adapter
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _ -> new")
	@NotNull public static Key<IntRule> registerInt(
			String name, Category category, int initialValue, BiConsumer<MinecraftServer, IntRule> changeCallback,
			GameruleValidatorAdapter<Integer> validatorAdapter) {
        return register(name, category, createInt(initialValue, changeCallback, validatorAdapter, validatorAdapter));
	}

	/**
	 * Creates and registers a new vanilla integer gamerule with the
	 * provided default value, at the specified name and in the specified
	 * category, with the supplied value validator and adapter.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param validatorAdapter The new gamerule's validator and adapter
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _ -> new")
	@NotNull public static Key<IntRule> registerInt(
			String name, Category category, int initialValue, GameruleValidatorAdapter<Integer> validatorAdapter
	) {
		return register(name, category, createInt(initialValue, empty(), validatorAdapter, validatorAdapter));
	}

	/**
	 * Creates and registers a new vanilla integer gamerule with the
	 * provided default value and change callback, at the specified
	 * name and in the specified category, with the supplied value
	 * validator and adapter.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _, _ -> new")
	@NotNull public static Key<IntRule> registerInt(
			String name, Category category, int initialValue, BiConsumer<MinecraftServer, IntRule> changeCallback,
			IGameruleValidator<Integer> validator, IGameruleAdapter<Integer> adapter) {
		return register(name, category, createInt(initialValue, changeCallback, validator, adapter));
	}

	/**
	 * Creates and registers a new vanilla integer gamerule with the
	 * provided default value and change callback, at the specified
	 * name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _ -> new")
	@NotNull public static Key<IntRule> registerInt(
			String name, Category category, int initialValue, BiConsumer<MinecraftServer, IntRule> changeCallback
	) {
		return register(name, category, createInt(initialValue, changeCallback));
	}

	/**
	 * Creates and registers a new vanilla integer gamerule with the
	 * provided default value, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _ -> new")
	@NotNull public static Key<IntRule> registerInt(
			String name, Category category, int initialValue, IGameruleValidator<Integer> validator,
			IGameruleAdapter<Integer> adapter
	) {
		return register(name, category, createInt(initialValue, validator, adapter));
	}

	/**
	 * Creates and registers a new vanilla integer gamerule with the
	 * provided default value, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _ -> new")
	@NotNull public static Key<IntRule> registerInt(String name, Category category, int initialValue) {
		return register(name, category, createInt(initialValue));
	}

	/**
	 * Creates a new Long gamerule, able to hold values from {@link Long#MIN_VALUE} to
	 * {@link Long#MAX_VALUE}, with the provided default value and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _, _ -> new", pure = true)
	@NotNull public static Type<LongRule> createLong(
			long initialValue, BiConsumer<MinecraftServer, LongRule> changeCallback,
			IGameruleValidator<Long> validator, IGameruleAdapter<Long> adapter
	) {
		return LongRule.create(initialValue, changeCallback, validator, adapter);
	}

	/**
	 * Creates a new Long gamerule, able to hold values from {@link Long#MIN_VALUE} to
	 * {@link Long#MAX_VALUE}, with the provided default value and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static Type<LongRule> createLong(
			long initialValue, BiConsumer<MinecraftServer, LongRule> changeCallback
	) {
		return LongRule.create(initialValue, changeCallback);
	}

	/**
	 * Creates a new Long gamerule, able to hold values from {@link Long#MIN_VALUE} to
	 * {@link Long#MAX_VALUE}, with the provided default value.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _ -> new", pure = true)
	@NotNull public static Type<LongRule> createLong(
			long initialValue, IGameruleValidator<Long> validator, IGameruleAdapter<Long> adapter
	) {
		return LongRule.create(initialValue, validator, adapter);
	}

	/**
	 * Creates a new Long gamerule, able to hold values from {@link Long#MIN_VALUE} to
	 * {@link Long#MAX_VALUE}, with the provided default value.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_ -> new", pure = true)
	@NotNull public static Type<LongRule> createLong(long initialValue) {
		return LongRule.create(initialValue);
	}

	/**
	 * Creates and registers a new Long gamerule, able to hold values
	 * from {@link Long#MIN_VALUE} to {@link Long#MAX_VALUE}, with
	 * the provided default value and change callback, at the
	 * specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _, _ -> new")
	@NotNull public static Key<LongRule> registerLong(
			String name, Category category, long initialValue, BiConsumer<MinecraftServer, LongRule> changeCallback,
			IGameruleValidator<Long> validator, IGameruleAdapter<Long> adapter
	) {
		return register(name, category, createLong(initialValue, changeCallback, validator, adapter));
	}

	/**
	 * Creates and registers a new Long gamerule, able to hold values
	 * from {@link Long#MIN_VALUE} to {@link Long#MAX_VALUE}, with
	 * the provided default value and change callback, at the
	 * specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _ -> new")
	@NotNull public static Key<LongRule> registerLong(
			String name, Category category, long initialValue, BiConsumer<MinecraftServer, LongRule> changeCallback
	) {
		return register(name, category, createLong(initialValue, changeCallback));
	}

	/**
	 * Creates and registers a new Long gamerule, able to hold values
	 * from {@link Long#MIN_VALUE} to {@link Long#MAX_VALUE}, with
	 * the provided default value, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _ -> new")
	@NotNull public static Key<LongRule> registerLong(
			String name, Category category, long initialValue, IGameruleValidator<Long> validator,
			IGameruleAdapter<Long> adapter
	) {
		return register(name, category, createLong(initialValue, validator, adapter));
	}

	/**
	 * Creates and registers a new Long gamerule, able to hold values
	 * from {@link Long#MIN_VALUE} to {@link Long#MAX_VALUE}, with
	 * the provided default value, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _ -> new")
	@NotNull public static Key<LongRule> registerLong(String name, Category category, long initialValue) {
		return register(name, category, createLong(initialValue));
	}

	/**
	 * Creates a new String gamerule with the specified default value, maximum length
	 * and change callback.
	 * <p>
	 * Warning: string gamerules can only have a maximum length up to 128. To go beyond,
	 * please look into text gamerules.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _, _, _ -> new")
	@NotNull public static Type<StringRule> createString(
			int maxLength, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback,
			IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
	) {
		return StringRule.create(maxLength, initialValue, changeCallback, validator, adapter);
	}

	/**
	 * Creates a new String gamerule with the specified default value, maximum length
	 * and change callback.
	 * <p>
	 * Warning: string gamerules can only have a maximum length up to 128. To go beyond,
	 * please look into text gamerules.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _ -> new")
	@NotNull public static Type<StringRule> createString(
			int maxLength, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback
	) {
		return StringRule.create(maxLength, initialValue, changeCallback);
	}

	/**
	 * Creates a new String gamerule with the specified default value and maximum length.
	 * <p>
	 * Warning: string gamerules can only have a maximum length up to 128. To go beyond,
	 * please look into text gamerules.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _, _ -> new")
	@NotNull public static Type<StringRule> createString(
			int maxLength, String initialValue, IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
	) {
		return StringRule.create(maxLength, initialValue, validator, adapter);
	}

	/**
	 * Creates a new String gamerule with the specified default value and maximum length.
	 * <p>
	 * Warning: string gamerules can only have a maximum length up to 128. To go beyond,
	 * please look into text gamerules.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new")
	@NotNull public static Type<StringRule> createString(int maxLength, String initialValue) {
		return StringRule.create(maxLength, initialValue);
	}

	/**
	 * Creates a new String gamerule with the specified default value and change callback,
	 * with a maximum length of either 32 or the initial value's length.
	 * <p>
	 * Warning: string gamerules can only have a maximum length up to 128. To go beyond,
	 * please look into text gamerules.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _, _ -> new")
	@NotNull public static Type<StringRule> createString(
			String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback,
			IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
	) {
		return StringRule.create(strLen(initialValue, 32), initialValue, changeCallback, validator, adapter);
	}

	/**
	 * Creates a new String gamerule with the specified default value and change callback,
	 * with a maximum length of either 32 or the initial value's length.
	 * <p>
	 * Warning: string gamerules can only have a maximum length up to 128. To go beyond,
	 * please look into text gamerules.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new")
	@NotNull public static Type<StringRule> createString(
			String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback
	) {
		return StringRule.create(strLen(initialValue, 32), initialValue, changeCallback);
	}

	/**
	 * Creates a new String gamerule with the specified default value and with a maximum length of
	 * either 32 or the initial value's length.
	 * <p>
	 * Warning: string gamerules can only have a maximum length up to 128. To go beyond,
	 * please look into text gamerules.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _ -> new", pure = true)
	@NotNull public static Type<StringRule> createString(
			String initialValue, IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
	) {
		return StringRule.create(strLen(initialValue, 32), initialValue, validator, adapter);
	}

	/**
	 * Creates a new String gamerule with the specified default value and with a maximum length of
	 * either 32 or the initial value's length.
	 * <p>
	 * Warning: string gamerules can only have a maximum length up to 128. To go beyond,
	 * please look into text gamerules.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_ -> new", pure = true)
	@NotNull public static Type<StringRule> createString(String initialValue) {
		return StringRule.create(strLen(initialValue, 32), initialValue);
	}

	/**
	 * Creates and registers a new String gamerule with the specified default value,
	 * maximum length and change callback, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerString(
			String name, Category category, int maxLength, String initialValue,
			BiConsumer<MinecraftServer, StringRule> changeCallback, IGameruleValidator<String> validator,
			IGameruleAdapter<String> adapter
	) {
		return register(name, category, createString(maxLength, initialValue, changeCallback, validator, adapter));
	}

	/**
	 * Creates and registers a new String gamerule with the specified default value,
	 * maximum length and change callback, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerString(
			String name, Category category, int maxLength, String initialValue,
			BiConsumer<MinecraftServer, StringRule> changeCallback
	) {
		return register(name, category, createString(maxLength, initialValue, changeCallback));
	}

	/**
	 * Creates and registers a new String gamerule with the specified default value
	 * and change callback, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerString(
			String name, Category category, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback,
			IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
	) {
		return register(name, category, createString(initialValue, changeCallback, validator, adapter));
	}

	/**
	 * Creates and registers a new String gamerule with the specified default value
	 * and change callback, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerString(
			String name, Category category, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback
	) {
		return register(name, category, createString(initialValue, changeCallback));
	}

	/**
	 * Creates and registers a new String gamerule with the specified default value
	 * and maximum length, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerString(
			String name, Category category, int maxLength, String initialValue, IGameruleValidator<String> validator,
			IGameruleAdapter<String> adapter
	) {
		return register(name, category, createString(maxLength, initialValue, validator, adapter));
	}

	/**
	 * Creates and registers a new String gamerule with the specified default value
	 * and maximum length, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerString(
			String name, Category category, int maxLength, String initialValue
	) {
		return register(name, category, createString(maxLength, initialValue));
	}

	/**
	 * Creates and registers a new String gamerule with the specified default value,
	 * at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerString(
			String name, Category category, String initialValue, IGameruleValidator<String> validator,
			IGameruleAdapter<String> adapter
	) {
		return register(name, category, createString(initialValue, validator, adapter));
	}

	/**
	 * Creates and registers a new String gamerule with the specified default value,
	 * at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _ -> new")
	@NotNull public static Key<StringRule> registerString(String name, Category category, String initialValue) {
		return register(name, category, createString(initialValue));
	}

	/**
	 * Creates a new Text gamerule with the specified default value, maximum length
	 * and change callback.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _, _, _ -> new", pure = true)
	@NotNull public static Type<StringRule> createText(
			int maxLength, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback,
			IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
	) {
		return StringRule.createText(maxLength, initialValue, changeCallback, validator, adapter);
	}

	/**
	 * Creates a new Text gamerule with the specified default value, maximum length
	 * and change callback.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _ -> new", pure = true)
	@NotNull public static Type<StringRule> createText(
			int maxLength, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback
	) {
		return StringRule.createText(maxLength, initialValue, changeCallback);
	}

	/**
	 * Creates a new Text gamerule with the specified default value and maximum length.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _, _ -> new", pure = true)
	@NotNull public static Type<StringRule> createText(
			int maxLength, String initialValue, IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
	) {
		return StringRule.createText(maxLength, initialValue, validator, adapter);
	}

	/**
	 * Creates a new Text gamerule with the specified default value and maximum length.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static Type<StringRule> createText(int maxLength, String initialValue) {
		return StringRule.createText(maxLength, initialValue);
	}

	/**
	 * Creates a new Text gamerule with the specified default value and change callback,
	 * with a maximum length of 512.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _, _ -> new", pure = true)
	@NotNull public static Type<StringRule> createText(
			String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback,
			IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
	) {
		return StringRule.createText(strLen(initialValue, 512), initialValue, changeCallback, validator, adapter);
	}

	/**
	 * Creates a new Text gamerule with the specified default value and change callback,
	 * with a maximum length of 512.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static Type<StringRule> createText(
			String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback
	) {
		return StringRule.createText(strLen(initialValue, 512), initialValue, changeCallback);
	}

	/**
	 * Creates a new Text gamerule with the specified default value.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _, _ -> new", pure = true)
	@NotNull public static Type<StringRule> createText(
			String initialValue, IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
	) {
		return StringRule.createText(strLen(initialValue, 512), initialValue, validator, adapter);
	}

	/**
	 * Creates a new Text gamerule with the specified default value.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_ -> new", pure = true)
	@NotNull public static Type<StringRule> createText(String initialValue) {
		return StringRule.createText(strLen(initialValue, 512), initialValue);
	}

	/**
	 * Creates and registers a new Text gamerule with the specified default value,
	 * maximum length and change callback, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerText(
			String name, Category category, int maxLength, String initialValue,
			BiConsumer<MinecraftServer, StringRule> changeCallback, IGameruleValidator<String> validator,
			IGameruleAdapter<String> adapter
	) {
		return register(name, category, createText(maxLength, initialValue, changeCallback, validator, adapter));
	}

	/**
	 * Creates and registers a new Text gamerule with the specified default value,
	 * maximum length and change callback, at the specified name and in the
	 * specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerText(
			String name, Category category, int maxLength, String initialValue,
			BiConsumer<MinecraftServer, StringRule> changeCallback
	) {
		return register(name, category, createText(maxLength, initialValue, changeCallback));
	}

	/**
	 * Creates and registers a new Text gamerule with the specified default value
	 * and change callback, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerText(
			String name, Category category, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback,
			IGameruleValidator<String> validator, IGameruleAdapter<String> adapter
	) {
		return register(name, category, createText(initialValue, changeCallback, validator, adapter));
	}

	/**
	 * Creates and registers a new Text gamerule with the specified default value
	 * and change callback, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerText(
			String name, Category category, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback
	) {
		return register(name, category, createText(initialValue, changeCallback));
	}

	/**
	 * Creates and registers a new Text gamerule with the specified default value and
	 * maximum length, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerText(
			String name, Category category, int maxLength, String initialValue, IGameruleValidator<String> validator,
			IGameruleAdapter<String> adapter
	) {
		return register(name, category, createText(maxLength, initialValue, validator, adapter));
	}

	/**
	 * Creates and registers a new Text gamerule with the specified default value and
	 * maximum length, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerText(
			String name, Category category, int maxLength, String initialValue
	) {
		return register(name, category, createText(maxLength, initialValue));
	}

	/**
	 * Creates and registers a new Text gamerule with the specified default value,
	 * at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _ -> new")
	@NotNull public static Key<StringRule> registerText(
			String name, Category category, String initialValue, IGameruleValidator<String> validator,
			IGameruleAdapter<String> adapter
	) {
		return register(name, category, createText(initialValue, validator, adapter));
	}

	/**
	 * Creates and registers a new Text gamerule with the specified default value,
	 * at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _ -> new")
	@NotNull public static Key<StringRule> registerText(String name, Category category, String initialValue) {
		return register(name, category, createText(initialValue));
	}

	/**
	 * Creates a new Entity Selector gamerule with the specified default value and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract("_, _, _, _ -> new")
	@NotNull public static Type<EntitySelectorRule> createEntitySelector(
			String initialValue, BiConsumer<MinecraftServer, EntitySelectorRule> changeCallback,
			IGameruleValidator<EntitySelector> validator, IGameruleAdapter<EntitySelector> adapter
	) {
		return EntitySelectorRule.create(initialValue, changeCallback, validator, adapter);
	}

	/**
	 * Creates a new Entity Selector gamerule with the specified default value and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract("_, _ -> new")
	@NotNull public static Type<EntitySelectorRule> createEntitySelector(
			String initialValue, BiConsumer<MinecraftServer, EntitySelectorRule> changeCallback
	) {
		return EntitySelectorRule.create(initialValue, changeCallback);
	}

	/**
	 * Creates a new Entity Selector gamerule with the specified default value.
	 * @param initialValue The new gamerule's default value.
	 * @param validator The new gamerule's validator.
	 * @param adapter The new gamerule's adapter.
	 * @return The newly created gamerule's reference.
	 */
	@Contract("_, _, _ -> new")
	@NotNull public static Type<EntitySelectorRule> createEntitySelector(
			String initialValue, IGameruleValidator<EntitySelector> validator, IGameruleAdapter<EntitySelector> adapter
	) {
		return EntitySelectorRule.create(initialValue, validator, adapter);
	}

	/**
	 * Creates a new Entity Selector gamerule with the specified default value.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract("_ -> new")
	@NotNull public static Type<EntitySelectorRule> createEntitySelector(String initialValue) {
		return EntitySelectorRule.create(initialValue);
	}

	/**
	 * Creates and registers a new Entity Selector gamerule with the specified default value
	 * and change callback, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _, _ -> new")
	@NotNull public static Key<EntitySelectorRule> registerEntitySelectorRule(
			String name, Category category, String initialValue,
			BiConsumer<MinecraftServer, EntitySelectorRule> changeCallback,
			IGameruleValidator<EntitySelector> validator, IGameruleAdapter<EntitySelector> adapter
	) {
		return register(name, category, createEntitySelector(initialValue, changeCallback, validator, adapter));
	}

	/**
	 * Creates and registers a new Entity Selector gamerule with the specified default value
	 * and change callback, at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _ -> new")
	@NotNull public static Key<EntitySelectorRule> registerEntitySelectorRule(
			String name, Category category, String initialValue,
			BiConsumer<MinecraftServer, EntitySelectorRule> changeCallback
	) {
		return register(name, category, createEntitySelector(initialValue, changeCallback));
	}

	/**
	 * Creates and registers a new Entity Selector gamerule with the specified default value,
	 * at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _, _, _ -> new")
	@NotNull public static Key<EntitySelectorRule> registerEntitySelectorRule(
			String name, Category category, String initialValue,
			IGameruleValidator<EntitySelector> validator, IGameruleAdapter<EntitySelector> adapter
	) {
		return register(name, category, createEntitySelector(initialValue, validator, adapter));
	}

	/**
	 * Creates and registers a new Entity Selector gamerule with the specified default value,
	 * at the specified name and in the specified category.
	 * @param name The name of the rule to register.
	 * @param category The category in which to register the gamerule.
	 * @param initialValue The new gamerule's default value.
	 * @return The new rule registration key.
	 */
	@Contract("_, _, _ -> new")
	@NotNull public static Key<EntitySelectorRule> registerEntitySelectorRule(
			String name, Category category, String initialValue
	) {
		return register(name, category, createEntitySelector(initialValue));
	}
}
