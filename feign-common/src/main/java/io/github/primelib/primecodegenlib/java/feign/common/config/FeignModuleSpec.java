package io.github.primelib.primecodegenlib.java.feign.common.config;

import io.github.primelib.primecodegenlib.java.common.util.ServiceLoaderUtil;
import io.github.primelib.primecodegenlib.java.feign.common.api.PrimeExtension;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Data
@Accessors(fluent = true)
@ApiStatus.Experimental
public class FeignModuleSpec<T extends FeignModuleSpec<T>> {

    /**
     * Proxy Configuration
     */
    @Nullable
    @Setter(AccessLevel.NONE)
    protected ProxySpec proxy = null;

    /**
     * Extensions to customize the behavior of the generated client.
     */
    @NotNull
    @Setter(AccessLevel.NONE)
    protected List<PrimeExtension> extensions = new ArrayList<>(2);

    /**
     * Sets the proxy configuration.
     *
     * @param proxySpec the proxy configuration
     * @return the proxy configuration
     */
    public T proxy(Consumer<ProxySpec> proxySpec) {
        this.proxy = new ProxySpec(proxySpec);
        return (T) this;
    }

    /**
     * Registers a module.
     * @param module the module
     */
    public T registerExtension(PrimeExtension module) {
        this.extensions.add(module);
        return (T) this;
    }

    /**
     * Convenience method that will register all extensions found on the classpath
     * <p>
     * You might have to use {@link #registerExtension(PrimeExtension)} if you want to customize the extension configuration.
     * Note: that method does not do any caching, so calls should be considered potentially expensive.
     * @see ServiceLoaderUtil#find(Class)
     */
    public T findAndRegisterExtensions() {
        ServiceLoaderUtil.find(PrimeExtension.class).forEach(this::registerExtension);
        return (T) this;
    }
}
