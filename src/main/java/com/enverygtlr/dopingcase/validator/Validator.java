package com.enverygtlr.dopingcase.validator;

public interface Validator<T> {
    void validate(T t);
}