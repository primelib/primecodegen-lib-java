package io.github.primelib.primecodegenlib.java.feign.common.capabilities;

import feign.InvocationHandlerFactory.MethodHandler;
import feign.Target;
import feign.Util;
import io.github.primelib.primecodegenlib.java.common.api.ThrowingFunction;
import io.github.primelib.primecodegenlib.java.feign.common.api.PrimeExtension;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An instance of {@link InvocationHandler} that uses {@link PrimeExtension}s to enhance the invocations of methods.
 */
@ApiStatus.Internal
public class PrimeInvocationHandler implements InvocationHandler {
    private final ExecutorService executor;
    private final Target<?> target;
    private final Map<Method, ThrowingFunction<Object[], Object, Throwable>> decoratedDispatch;

    public PrimeInvocationHandler(@NotNull String backendName, @NotNull Target<?> target, @NotNull Map<Method, MethodHandler> dispatch, @NotNull List<PrimeExtension> extensions) {
        this.target = Util.checkNotNull(target, "target");
        Util.checkNotNull(dispatch, "dispatch");

        // Use virtual threads if available, otherwise use a cached thread pool
        ExecutorService executor;
        try {
            Method newVirtualThreadPerTaskExecutor = Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
            executor = (ExecutorService) newVirtualThreadPerTaskExecutor.invoke(null);
        } catch (Exception ignored) {
            executor = Executors.newCachedThreadPool();
        }
        this.executor = executor;

        // decorate method invocations
        decoratedDispatch = new HashMap<>(dispatch.size());
        dispatch.forEach((method, methodHandler) -> {
            // wrap method invocation in ThrowingFunction
            ThrowingFunction<Object[], Object, Throwable> decoratedFunction = methodHandler::invoke;

            // decorate method invocations
            for (PrimeExtension extension : extensions) {
                decoratedFunction = extension.decorateFeignInvocation(backendName, decoratedFunction, dispatch, target);
            }
            decoratedDispatch.put(method, decoratedFunction);
        });
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        switch (method.getName()) {
            case "equals":
                return equals(args.length > 0 ? args[0] : null);
            case "hashCode":
                return hashCode();
            case "toString":
                return toString();
        }

        // wrap method invocation in CompletableFuture if needed
        if (CompletableFuture.class.isAssignableFrom(method.getReturnType())) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return decoratedDispatch.get(method).invoke(args);
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }, executor);
        }

        // invoke method
        return decoratedDispatch.get(method).invoke(args);
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
        if (compareTo instanceof PrimeInvocationHandler) {
            PrimeInvocationHandler other = (PrimeInvocationHandler) compareTo;
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
