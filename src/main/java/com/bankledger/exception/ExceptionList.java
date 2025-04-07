package com.bankledger.exception;

import java.util.List;
import java.util.Map;

public class ExceptionList extends RuntimeException {
    private final Map<String, List<String>> errors;

    public ExceptionList(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }
}