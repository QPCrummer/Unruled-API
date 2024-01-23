package mc.recraftors.unruled_api;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

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

	/**
	 * Creates a vanilla Boolean gamerule with the provided default value and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static GameRules.Type<GameRules.BooleanRule> createBoolean(boolean initialValue, BiConsumer<MinecraftServer, GameRules.BooleanRule> changeCallback) {
		return GameRules.BooleanRule.create(initialValue, changeCallback);
	}

	/**
	 * Creates a vanilla Boolean gamerule with the provided default value.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_ -> new", pure = true)
	@NotNull public static GameRules.Type<GameRules.BooleanRule> createBoolean(boolean initialValue) {
		return GameRules.BooleanRule.create(initialValue);
	}

	/**
	 * Creates a new Double gamerule, able to hold values from {@link Double#MIN_VALUE} to
	 * {@link Double#MAX_VALUE}, with the provided default value and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static GameRules.Type<DoubleRule> createDouble(double initialValue, BiConsumer<MinecraftServer, DoubleRule> changeCallback) {
		return DoubleRule.create(initialValue, changeCallback);
	}

	/**
	 * Creates a new Double gamerule, able to hold values from {@link Double#MIN_VALUE} to
	 * {@link Double#MAX_VALUE}, with the provided default value.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_ -> new", pure = true)
	@NotNull public static GameRules.Type<DoubleRule> createDouble(double initialValue) {
		return DoubleRule.create(initialValue);
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
	@NotNull public static <T extends Enum<T>> GameRules.Type<EnumRule<T>> createEnum(Class<T> tClass, T initialValue, BiConsumer<MinecraftServer, EnumRule<T>> changeCallback) {
		return EnumRule.create(tClass, initialValue, changeCallback);
	}

	/**
	 * Creates a new Enum gamerule of the specified class, with the provided default value.
	 * @param tClass The new gamerule's enum class.
	 * @param initialValue The new gamerule's default value. Must be of the provided enum class.
	 * @return The newly created gamerule's reference.
	 * @param <T> The new gamerule's enum type.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static <T extends Enum<T>> GameRules.Type<EnumRule<T>> createEnum(Class<T> tClass, T initialValue) {
		return EnumRule.create(tClass, initialValue);
	}

	/**
	 * Creates a new Float gamerule, able to hold values from {@link Float#MIN_VALUE} to
	 * {@link Float#MAX_VALUE}, with the provided default value and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static GameRules.Type<FloatRule> createFloat(float initialValue, BiConsumer<MinecraftServer, FloatRule> changeCallback) {
		return FloatRule.create(initialValue, changeCallback);
	}

	/**
	 * Creates a new Float gamerule, able to hold values from {@link Float#MIN_VALUE} to
	 * {@link Float#MAX_VALUE}, with the provided default value.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_ -> new", pure = true)
	@NotNull public static GameRules.Type<FloatRule> createFloat(float initialValue) {
		return FloatRule.create(initialValue);
	}

	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static GameRules.Type<GameRules.IntRule> createInt(int initialValue, BiConsumer<MinecraftServer, GameRules.IntRule> changeCallback) {
		return GameRules.IntRule.create(initialValue, changeCallback);
	}

	@Contract(value = "_ -> new", pure = true)
	@NotNull public static GameRules.Type<GameRules.IntRule> createInt(int initialValue) {
		return GameRules.IntRule.create(initialValue);
	}

	/**
	 * Creates a new Long gamerule, able to hold values from {@link Long#MIN_VALUE} to
	 * {@link Long#MAX_VALUE}, with the provided default value and change callback.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static GameRules.Type<LongRule> createLong(long initialValue, BiConsumer<MinecraftServer, LongRule> changeCallback) {
		return LongRule.create(initialValue, changeCallback);
	}

	/**
	 * Creates a new Long gamerule, able to hold values from {@link Long#MIN_VALUE} to
	 * {@link Long#MAX_VALUE}, with the provided default value.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_ -> new", pure = true)
	@NotNull public static GameRules.Type<LongRule> createLong(long initialValue) {
		return LongRule.create(initialValue);
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
	@Contract(value = "_, _, _ -> new", pure = true)
	@NotNull public static GameRules.Type<StringRule> createString(int maxLength, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback) {
		return StringRule.create(maxLength, initialValue, changeCallback);
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
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static GameRules.Type<StringRule> createString(int maxLength, String initialValue) {
		return StringRule.create(maxLength, initialValue);
	}

	/**
	 * Creates a new String gamerule with the specified default value and change callback,
	 * with a maximum length of 32.
	 * <p>
	 * Warning: string gamerules can only have a maximum length up to 128. To go beyond,
	 * please look into text gamerules.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static GameRules.Type<StringRule> createString(String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback) {
		return StringRule.create(32, initialValue, changeCallback);
	}

	/**
	 * Creates a new String gamerule with the specified default value and with a maximum length of 32.
	 * <p>
	 * Warning: string gamerules can only have a maximum length up to 128. To go beyond,
	 * please look into text gamerules.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_ -> new", pure = true)
	@NotNull public static GameRules.Type<StringRule> createString(String initialValue) {
		return StringRule.create(32, initialValue);
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
	@NotNull public static GameRules.Type<StringRule> createText(int maxLength, String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback) {
		return StringRule.createText(maxLength, initialValue, changeCallback);
	}

	/**
	 * Creates a new Text gamerule with the specified default value and maximum length.
	 * @param maxLength The new gamerule's maximum length.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static GameRules.Type<StringRule> createText(int maxLength, String initialValue) {
		return StringRule.createText(maxLength, initialValue);
	}

	/**
	 * Creates a new Text gamerule with the specified default value and change callback,
	 * with a maximum length of 512.
	 * @param initialValue The new gamerule's default value.
	 * @param changeCallback The new gamerule's change callback.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_, _ -> new", pure = true)
	@NotNull public static GameRules.Type<StringRule> createText(String initialValue, BiConsumer<MinecraftServer, StringRule> changeCallback) {
		return StringRule.createText(512, initialValue, changeCallback);
	}

	/**
	 * Creates a new Text gamerule with the specified default value.
	 * @param initialValue The new gamerule's default value.
	 * @return The newly created gamerule's reference.
	 */
	@Contract(value = "_ -> new", pure = true)
	@NotNull public static GameRules.Type<StringRule> createText(String initialValue) {
		return StringRule.createText(512, initialValue);
	}
}
