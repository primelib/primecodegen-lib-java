package io.github.primelib.primecodegenlib.java.feign.common.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

@Data
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Slf4j
public class ProxySpec {
    /**
     * The type of the proxy server
     */
    @NotNull
    private Proxy.Type type = Proxy.Type.HTTP;

    /**
     * The host of the proxy server
     */
    private String host;

    /**
     * The port of the proxy server
     */
    private Integer port;

    /**
     * The username used to authenticate with the proxy, if applicable
     */
    private String username;

    /**
     * The password used to authenticate with the proxy, if applicable
     */
    private char[] password;

    /**
     * Constructs a validated implementation of {@link ProxySpec}.
     *
     * @param spec the specification to process
     */
    @ApiStatus.Internal
    public ProxySpec(Consumer<ProxySpec> spec) {
        spec.accept(this);
        validate();
    }

    /**
     * Validates the Spec, will throw a exception if required parameters are missing
     *
     * @throws NullPointerException if a required parameter is missing
     * @throws IllegalArgumentException if a parameter has an invalid value
     */
    public void validate() {
        Objects.requireNonNull(type, "type is a required parameter!");
        if (type == Proxy.Type.HTTP) {
            Objects.requireNonNull(host, "host is a required parameter!");
            Objects.requireNonNull(port, "port is a required parameter!");
        }
    }

    /**
     * Creates a new {@link ProxySpec} with values taken from the proxy environment variables, if applicable.
     */
    public static ProxySpec detect() {
        Set<String> proxies = new HashSet<>(2);
        proxies.add(System.getenv("http_proxy"));
        proxies.add(System.getenv("https_proxy"));

        // check if a proxy is configured
        for (String p : proxies) {
            try {
                URI proxyUri = new URI(p);
                return new ProxySpec(spec -> {
                    spec.type(Proxy.Type.HTTP);
                    spec.host(proxyUri.getHost());
                    spec.port(proxyUri.getPort() == -1 ? 80 : proxyUri.getPort());
                });
            } catch (URISyntaxException ignored) {
                // ignore invalid proxy settings
            }
        }

        // no proxy configured
        return new ProxySpec(spec -> {
            spec.type(Proxy.Type.DIRECT);
        });
    }
}
