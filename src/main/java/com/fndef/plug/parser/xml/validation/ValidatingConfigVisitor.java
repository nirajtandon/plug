package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Errors;
import com.fndef.plug.parser.xml.XmlConfigVisitor;

public interface ValidatingConfigVisitor extends XmlConfigVisitor {
    Errors getErrors();
}
