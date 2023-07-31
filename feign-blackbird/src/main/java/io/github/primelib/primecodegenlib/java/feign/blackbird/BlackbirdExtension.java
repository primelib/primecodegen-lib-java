package io.github.primelib.primecodegenlib.java.feign.blackbird;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import io.github.primelib.primecodegenlib.java.feign.common.api.PrimeExtension;
import org.jetbrains.annotations.NotNull;

/**
 * Blackbird Module
 * <p>
 * This module adds the Blackbird Module to the Jackson ObjectMapper.
 */
public class BlackbirdExtension implements PrimeExtension {
    public BlackbirdExtension() {

    }

    @Override
    public void customizeObjectMapper(JsonMapper.@NotNull Builder builder) {
        builder.addModule(new BlackbirdModule());
    }
}
