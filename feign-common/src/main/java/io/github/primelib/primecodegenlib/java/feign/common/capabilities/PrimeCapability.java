package io.github.primelib.primecodegenlib.java.feign.common.capabilities;

import feign.Capability;
import feign.Contract;
import feign.InvocationHandlerFactory;
import io.github.primelib.primecodegenlib.java.feign.common.api.PrimeExtension;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PrimeCapability implements Capability {
    private final String backendName;
    private final List<PrimeExtension> modules;

    public PrimeCapability(@NotNull String backendName, @NotNull List<PrimeExtension> modules) {
        this.backendName = backendName;
        this.modules = modules;
    }

    @Override
    public Contract enrich(Contract contract) {
        return new PrimeDelegatingContract(contract);
    }

    @Override
    public InvocationHandlerFactory enrich(InvocationHandlerFactory invocationHandlerFactory) {
        return (target, dispatch) -> new PrimeInvocationHandler(backendName, target, dispatch, modules);
    }
}
