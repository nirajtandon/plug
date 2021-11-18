package com.fndef.plug.parser.xml;

import com.fndef.plug.common.AggregateWalker;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static com.fndef.plug.parser.xml.TagHandler.defaultHandler;

class DomWalker implements AggregateWalker<XmlConfig> {
    private final TagHandler tagHandler;
    private final Document configDocument;
    DomWalker(Document document) {
        this.configDocument = document;
        this.tagHandler = defaultHandler();
    }

    public XmlConfig walk() {
        return buildConfig(configDocument.getDocumentElement(), null);
    }

    private XmlConfig buildConfig(Node node, XmlConfig parent) {
        XmlConfig entry = tagHandler.entryFor(node);
        entry.setParent(parent);
        NodeList childNodes = node.getChildNodes();
        for(int k = 0; k < childNodes.getLength(); k++) {
            Node n = childNodes.item(k);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                entry.addEntry(buildConfig(n, entry));
            }
        }
        return entry;
    }
}
