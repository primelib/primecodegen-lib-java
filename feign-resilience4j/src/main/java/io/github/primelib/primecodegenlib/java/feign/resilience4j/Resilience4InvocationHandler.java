package io.github.primelib.primecodegenlib.java.feign.resilience4j;

import feign.InvocationHandlerFactory.MethodHandler;
import feign.Target;
import feign.Util;
import io.github.resilience4j.core.functions.CheckedFunction;
import io.github.resilience4j.feign.FeignDecorator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An instance of {@link InvocationHandler} that uses {@link FeignDecorator}s to enhance the invocations of methods.
 */
@ApiStatus.Internal
public class Resilience4InvocationHandler implements InvocationHandler {
    private final ExecutorService executor;
    private final Target<?> target;
    private final Map<Method, CheckedFunction<Object[], Object>> decoratedDispatch;

    public Resilience4InvocationHandler(@NotNull Target<?> target, @NotNull Map<Method, MethodHandler> dispatch, FeignDecorator invocationDecorator) {
        this.target = Util.checkNotNull(target, "target");
        Util.checkNotNull(dispatch, "dispatch");
        this.decoratedDispatch = decorateMethodHandlers(dispatch, invocationDecorator, target);

        // Use virtual threads if available, otherwise use a cached thread pool
        ExecutorService executor;
        try {
            Method newVirtualThreadPerTaskExecutor = Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
            executor = (ExecutorService) newVirtualThreadPerTaskExecutor.invoke(null);
        } catch (Exception ignored) {
            executor = Executors.newCachedThreadPool();
        }
        this.executor = executor;
    }

    /**
     * Applies the specified {@link FeignDecorator} to all specified {@link MethodHandler}s and
     * returns the result as a map of {@link CheckedFunction}s. Invoking a {@link CheckedFunction}
     * will therefore invoke the decorator which, in turn, may invoke the corresponding {@link
     * MethodHandler}.
     *
     * @param dispatch            a map of the methods from the feign interface to the {@link
     *                            MethodHandler}s.
     * @param invocationDecorator the {@link FeignDecorator} with which to decorate the {@link
     *                            MethodHandler}s.
     * @param target              the target feign interface.
     * @return a new map where the {@link MethodHandler}s are decorated with the {@link
     * FeignDecorator}.
     */
    private Map<Method, CheckedFunction<Object[], Object>> decorateMethodHandlers(Map<Method, MethodHandler> dispatch, FeignDecorator invocationDecorator, Target<?> target) {
        final Map<Method, CheckedFunction<Object[], Object>> map = new HashMap<>();
        for (final Map.Entry<Method, MethodHandler> entry : dispatch.entrySet()) {
            final Method method = entry.getKey();
            final MethodHandler methodHandler = entry.getValue();
            if (methodHandler != null) {
                CheckedFunction<Object[], Object> decorated = invocationDecorator.decorate(methodHandler::invoke, method, methodHandler, target);
                map.put(method, decorated);
            }
        }
        return map;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        switch (method.getName()) {
            case "equals" -> {
                return equals(args.length > 0 ? args[0] : null);
            }
            case "hashCode" -> {
                return hashCode();
            }
            case "toString" -> {
                return toString();
            }
            default -> {
            }
        }

        // wrap method invocation in CompletableFuture if needed
        if (CompletableFuture.class.isAssignableFrom(method.getReturnType())) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return decoratedDispatch.get(method).apply(args);
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }, executor);
        }

        // invoke method
        return decoratedDispatch.get(method).apply(args);
    }

    @Override
    public boolean equals(Object obj) {
        Object compareTo = obj;
        if (compareTo == null) {
            return false;
        }
        if (Proxy.isProxyClass(compareTo.getClass())) {
            compareTo = Proxy.getInvocationHandler(compareTo);
        }
        if (compareTo instanceof Resilience4InvocationHandler other) {
            return target.equals(other.target);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
