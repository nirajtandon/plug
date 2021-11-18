package com.fndef.plug.parser.xml;


import com.fndef.plug.common.ConfigOptions;
import com.fndef.plug.Context;
import com.fndef.plug.parser.ParsingStrategy;
import com.fndef.plug.parser.ResolveStrategy;
import com.fndef.plug.parser.StrategyProvider;

public class XmlConfigStrategyProvider implements StrategyProvider {

    private ParsingStrategy<XmlConfig> parsingStrategy;
    private ResolveStrategy<XmlConfig> resolverStrategy;

    public XmlConfigStrategyProvider(ConfigOptions options) {
        buildParsingStrategy(options);
        buildResolverStrategy(options);
    }

    private void buildResolverStrategy(ConfigOptions options) {
        resolverStrategy = new ResolveStrategy<XmlConfig>() {
            @Override
            public Context resolve(XmlConfig unresolvedConfig) {
                return  new ValidatingConfigResolver(options, unresolvedConfig)
                        .validateConfigStructure()
                        .validateConfigParameters()
                        .validateReferences()
                        .resolve();
            }
        };
    }

    private void buildParsingStrategy(ConfigOptions options) {
        parsingStrategy = new XmlDomParsingStrategy(options);
    }

    @Override
    public ParsingStrategy parsingStrategy() {
        return parsingStrategy;
    }

    public ResolveStrategy<XmlConfig> resolveStrategy() {
        return resolverStrategy;
    }
}
