package io.github.primelib.primecodegenlib.java.feign.common.api;

import com.fasterxml.jackson.databind.json.JsonMapper;
import feign.InvocationHandlerFactory;
import feign.Target;
import io.github.primelib.primecodegenlib.java.common.api.ThrowingFunction;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * A Prime Extension.
 */
@ApiStatus.Internal
public interface PrimeExtension {

    /**
     * Decorates the invocations of a function.
     *
     * @param backendName the name of the backend
     * @param function the function to decorate
     * @param dispatch the dispatch
     * @param target the target
     * @return the decorated method handler
     */
    @NotNull
    default ThrowingFunction<Object[], Object, Throwable> decorateFeignInvocation(String backendName, ThrowingFunction<Object[], Object, Throwable> function, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch, Target<?> target) {
        return function;
    }

    /**
     * Customize the Jackson ObjectMapper.
     *
     * @param builder the builder
     */
    default void customizeObjectMapper(@NotNull JsonMapper.Builder builder) {}
}
