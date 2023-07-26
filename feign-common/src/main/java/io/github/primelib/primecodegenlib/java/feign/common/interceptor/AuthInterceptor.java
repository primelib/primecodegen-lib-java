package io.github.primelib.primecodegenlib.java.feign.common.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.primelib.primecodegenlib.java.feign.common.api.AuthMethod;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class AuthInterceptor implements RequestInterceptor {
    List<AuthMethod> authMethods;

    @Override
    public void apply(RequestTemplate template) {
        if (authMethods == null || authMethods.isEmpty()) {
            return;
        }

        for (AuthMethod authMethod : authMethods) {
            if (authMethod.headerMap() != null) {
                authMethod.headerMap().forEach(template::header);
            }
            if (authMethod.queryMap() != null) {
                authMethod.queryMap().forEach(template::query);
            }
        }
    }
}
