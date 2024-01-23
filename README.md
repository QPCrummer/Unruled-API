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
        myGameRule = registerGamerule("my_gamerule", UnruledApi.createFloat(1.5));
        myOtherGameRule = registerGamerule("my_other_gamerule", UnruledApi.createString("some text"));
        myLastGameRule = registerGamerule("my_last_gamerule", UnruledApi.createBoolean(false, (server, rule) -> {
            onLastRuleChange(server, rule);
        }));
    }
```

Each rule can also be easily consulted using the `IGameRulesProvider` interface.

Example:
```java
        String value = ((IGameRulesProvider)gamerules).unruled_getString(myStringGameRule);
        float aFloat = ((IGameRulesProvider)gamerules).unruled_getFloat(myFloatGameRule);
```