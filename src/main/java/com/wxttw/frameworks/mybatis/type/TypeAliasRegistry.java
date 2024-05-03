package com.wxttw.frameworks.mybatis.type;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * @author jay
 * @date 2024/5/2 22:19
 * @description: TODO
 */
public class TypeAliasRegistry {

    private final Map<String, Class<?>> typeAliases = new HashMap<>();

    public void registerAlias(String alias, Class<?> value) {
        if (Objects.isNull(alias)) {
            throw new RuntimeException("The parameter alias cannot be null");
        }

        String key = alias.toLowerCase(Locale.ENGLISH);
        if (typeAliases.containsKey(key) && Objects.nonNull(typeAliases.get(key)) && !typeAliases.get(key).equals(value)) {
            throw new RuntimeException(
                    "The alias '" + alias + "' is already mapped to the value '" + typeAliases.get(key).getName() + "'.");
        }
        typeAliases.put(key, value);
    }

    public <T> Class<T> resolveAlias(String alias) {
        if (Objects.isNull(alias)) {
            return null;
        }
        String key = alias.toLowerCase(Locale.ENGLISH);
        if (typeAliases.containsKey(key)) {
            return (Class<T>) typeAliases.get(key);
        }
        throw new RuntimeException("Could not resolve type alias '" + alias + "'.");
    }
}
