package com.fndef.plug.parser;

import com.fndef.plug.common.ConfigOptions;
import com.fndef.plug.parser.xml.XmlConfigStrategyProvider;

public interface StrategyProvider {

    ParsingStrategy parsingStrategy();
    ResolveStrategy resolveStrategy();

    public static StrategyProvider of(ConfigOptions options) {
        switch (options.configFormat()) {
            case XML:
                return new XmlConfigStrategyProvider(options);
            case ANNOTATION:
            case DSL:
            default:
                throw new IllegalArgumentException("Unsupported config type - "+options.configFormat());
        }
    }
}
