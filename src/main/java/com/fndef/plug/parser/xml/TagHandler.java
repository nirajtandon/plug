package com.fndef.plug.parser.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public interface TagHandler {

    static TagHandler defaultHandler() {
        return new TagHandler() {};
    }

    default XmlConfig entryFor(Node xmlTag) {
        if (!TagType.isTagValid(xmlTag.getNodeName())) {
            throw new IllegalStateException("Invalid config - tag ["+xmlTag.getNodeName()+"] not supported");
        }

        XmlConfig entry = new XmlConfig(xmlTag.getNodeName(), TagType.tag(xmlTag.getNodeName()));
        NamedNodeMap attributes = xmlTag.getAttributes();
        for(int k = 0; k < attributes.getLength(); k++) {
            Node attr = attributes.item(k);
            entry.addAttribute(attr.getNodeName(), attr.getNodeValue());
        }
        return entry;
    }

    default String tagDescription(Node node) {
        return new StringBuilder()
                .append("{")
                .append(node.getNodeName())
                .append("}")
                .toString();
    }
}
