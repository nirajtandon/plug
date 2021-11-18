package com.fndef.plug.parser.xml;

import java.util.Objects;

import static com.fndef.plug.parser.xml.SimpleConfigWalker.WalkStrategy.PRE;

public class SimpleConfigWalker implements XmlConfigWalker {

    private final XmlConfig config;
    private final XmlConfigVisitor visitor;
    private final WalkStrategy walkStrategy;

    public SimpleConfigWalker(XmlConfig config, XmlConfigVisitor visitor) {
        this(config, visitor, PRE);
    }

    public SimpleConfigWalker(XmlConfig config, XmlConfigVisitor visitor, WalkStrategy walkStrategy) {
        Objects.requireNonNull(config, "Config can't be null");
        Objects.requireNonNull(visitor, "Config visitor can't be null");
        Objects.requireNonNull(walkStrategy, "Walk strategy can't be null");

        this.config = config;
        this.visitor = visitor;
        this.walkStrategy = walkStrategy;
    }

    @Override
    public XmlConfig getConfig() {
        return config;
    }

    @Override
    public XmlConfigVisitor getVisitor() {
        return visitor;
    }

    @Override
    public XmlConfig walk() {
        switch (walkStrategy) {
            case PRE:
                return walkPre(config);
            case POST:
                return walkPost(config);
            default:
                throw new IllegalArgumentException("Unsupported config walk strategy - "+walkStrategy);
        }
    }

    public enum WalkStrategy {
        PRE,
        POST
    }

}
