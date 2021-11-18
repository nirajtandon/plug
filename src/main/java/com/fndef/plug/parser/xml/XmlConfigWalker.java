package com.fndef.plug.parser.xml;

import com.fndef.plug.common.AggregateWalker;

import static com.fndef.plug.parser.xml.SimpleConfigWalker.WalkStrategy.PRE;

public interface XmlConfigWalker extends AggregateWalker<XmlConfig> {

    XmlConfig getConfig();
    XmlConfigVisitor getVisitor();

    static <T extends XmlConfigVisitor> T visitConfig(XmlConfig config, T visitor) {
        return visitConfig(config, visitor, PRE);
    }

    static <T extends XmlConfigVisitor> T visitConfig(XmlConfig config, T visitor, SimpleConfigWalker.WalkStrategy walkStrategy) {
        XmlConfigWalker walker = new SimpleConfigWalker(config, visitor, walkStrategy);
        walker.walk();
        return visitor;
    }

    default XmlConfig walkPre(XmlConfig xmlConfig) {
        xmlConfig = before(xmlConfig);
        xmlConfig.accept(getVisitor());
        for(XmlConfig xc : xmlConfig.getChildEntries()) {
            walkPre(xc);
        }
        return after(xmlConfig);
    }

    default XmlConfig walkPost(XmlConfig xmlConfig) {
        xmlConfig = before(xmlConfig);
        for(XmlConfig xc : xmlConfig.getChildEntries()) {
            walkPost(xc);
        }
        xmlConfig.accept(getVisitor());
        return after(xmlConfig);
    }

    default XmlConfig before(XmlConfig config) {
        return config;
    }

    default XmlConfig after(XmlConfig config) {
        return config;
    }
}
