package com.fndef.plug.parser.xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class DomConfigLoader {
    public Document load(InputStream is) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new DomParsingErrorHandler());
            return builder.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new IllegalStateException("Config DOM parsing failed", ex);
        }
    }
}
