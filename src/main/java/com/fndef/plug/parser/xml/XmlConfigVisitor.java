package com.fndef.plug.parser.xml;

public interface XmlConfigVisitor {
    void factoryConfig(XmlConfig config);
    void objectConfig(XmlConfig config);
    void componentConfig(XmlConfig config);
    void controlConfig(XmlConfig config);
    void constructorConfig(XmlConfig config);
    void invokeConfig(XmlConfig config);
    void configurationConfig(XmlConfig config);
}
