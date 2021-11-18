package com.fndef.plug.parser.xml.validation;

import com.fndef.plug.common.Error;
import com.fndef.plug.parser.xml.XmlConfig;

public class ValidationError implements Error {
    private XmlConfig config;
    private String errorMsg;

    public ValidationError(XmlConfig config, String errorMsg) {
        this.config = config;
        this.errorMsg = errorMsg;
    }

    public XmlConfig getConfig() {
        return config;
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    public String toString() {
        return "Validation error ["+getErrorMessage()+"]";
    }
}
