# Unruled API

<img src="common/src/main/resources/icon.png" alt="mod icon" height="318" width="318">

Allows to create new form of gamerules, beyond the restrictive vanilla integers and booleans.

Albeit not registering them automatically, this mod allows you to easily create new
floating, long, double, string, text and enum-driven gamerules, using the generative static
methods of the `mc.recraftors.unruled_api.UnruledApi` class.

For consistency, methods have also been created to allow instantiating vanilla gamerules.

Example:
```java
    public void gamerulesRegistration() {
        myGameRule = registerGamerule("my_gamerule", category, UnruledApi.createFloat(1.5));
        myOtherGameRule = registerGamerule("my_other_gamerule", category, UnruledApi.createString("some text"));
        myLastGameRule = registerGamerule("my_last_gamerule", category, UnruledApi.createBoolean(false, (server, rule) -> {
            onLastRuleChange(server, rule);
        }));
    }
```

Now, for your very own convenience (since 0.4), with additional creation and registration method!

```java
    public void registerCustomGamerules() {
        MY_BOOLEAN_RULE = UnruledApi.register("gamerule_name", someCategory, UnruledApi.createBoolean(false));
        MY_FLOAT_RULE = UnruledApi.registerFloat("other_gamerule_name", sameOrOtherCategory, 0.5);
        MY_ENUM_RULE = UnruledApi.registerEnum("yet_another_gamerule_name", yetAgainACategory, MyEnumClass.class, MyEnumClass.SOME_VALUE);
    }
```

Each rule can also be easily consulted using the `IGameRulesProvider` interface.

Example:
```java
        String value = ((IGameRulesProvider)gamerules).unruled_getString(myStringGameRule);
        float aFloat = ((IGameRulesProvider)gamerules).unruled_getFloat(myFloatGameRule);
```
