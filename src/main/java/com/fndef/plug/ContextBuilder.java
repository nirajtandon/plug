package com.fndef.plug;

import com.fndef.plug.common.ConfigOptions;
import com.fndef.plug.parser.ParsingStrategy;
import com.fndef.plug.parser.ResolveStrategy;
import com.fndef.plug.parser.StrategyProvider;

import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class ContextBuilder {

    private final ConfigOptions options;
    private final InputStream inputStreamConfig;
    private final StrategyProvider strategyProvider;
    private final AtomicBoolean contextCreated = new AtomicBoolean(false);
    private Context context;

    ContextBuilder(ConfigOptions options, InputStream inputStreamConfig) {
        this.options = options;
        this.inputStreamConfig = inputStreamConfig;
        strategyProvider = StrategyProvider.of(options);
    }

    private Context resolveContext(InputStream is) {
        Objects.requireNonNull(is, "No configuration specified");
        ParsingStrategy parser = strategyProvider.parsingStrategy();
        ResolveStrategy resolveStrategy = strategyProvider.resolveStrategy();
        return resolveStrategy.resolve(parser.parse(is));
    }

    private Context init() {
        if (contextCreated.compareAndSet(false, true)) {
            context = resolveContext(inputStreamConfig);
        }
        return context;
    }

    public static class WithOptions {
        private boolean frozen;
        private final ConfigOptions options;
        private InputStream inputStreamConfig;

        public WithOptions(ConfigFormat configFormat) {
            Objects.requireNonNull(configFormat,"Config format can't be null");
            options = new ConfigOptions(configFormat);
        }

        public Context getContext() {
            errorIfFrozen();
            frozen = true;
            validate();
            return new ContextBuilder(options, inputStreamConfig).init();
        }

        public WithOptions gatherStats(boolean stats) {
            errorIfFrozen();
            options.gatherStats(stats);
            return this;
        }

        public WithOptions configSource(InputStream is) {
            errorIfFrozen();
            this.inputStreamConfig = is;
            return this;
        }

        public WithOptions strictLookup(boolean sl) {
            errorIfFrozen();
            options.strictLookup(sl);
            return this;
        }

        public WithOptions keepResolvables(boolean kr) {
            errorIfFrozen();
            options.keepResolvables(kr);
            return this;
        }

        private void errorIfFrozen() {
            if (frozen) {
                throw new IllegalStateException("Context already created");
            }
        }

        private void validate() {
            Objects.requireNonNull(inputStreamConfig, "Config can't be null");
        }

    }
}
