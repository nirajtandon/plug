package com.fndef.plug.parser.xml;

import com.fndef.plug.common.ConfigOptions;
import com.fndef.plug.parser.ParsingStrategy;

import java.io.InputStream;
import java.util.Objects;

public class XmlDomParsingStrategy implements ParsingStrategy<XmlConfig> {

    private final ConfigOptions configOptions;
    public XmlDomParsingStrategy(ConfigOptions options) {
        this.configOptions = options;
    }

    @Override
    public XmlConfig parse(InputStream is) {
        Objects.requireNonNull(is, "Missing input config");
        return new DomWalker(new DomConfigLoader().load(is)).walk();
    }
}
