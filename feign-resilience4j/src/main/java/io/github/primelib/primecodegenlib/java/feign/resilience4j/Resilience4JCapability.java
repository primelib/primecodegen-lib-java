package io.github.primelib.primecodegenlib.java.feign.resilience4j;

import feign.Capability;
import feign.Contract;
import feign.InvocationHandlerFactory;
import io.github.resilience4j.feign.FeignDecorators;

public final class Resilience4JCapability implements Capability {
    private final FeignDecorators decorators;

    public Resilience4JCapability(FeignDecorators decorators) {
        this.decorators = decorators;
    }

    @Override
    public Contract enrich(Contract contract) {
        return new Resilience4DelegatingContract(contract);
    }

    @Override
    public InvocationHandlerFactory enrich(InvocationHandlerFactory invocationHandlerFactory) {
        return (target, dispatch) -> new Resilience4InvocationHandler(target, dispatch, decorators);
    }

}
