package org.auth1.auth1.core.authentication;

import java.util.Arrays;
import java.util.List;

public class Result<T extends Enum<T>> {
    private final T type;

    public Result(T type) {
        this.type = type;
    }

    protected void require(T type) {
        requireAny(type);
    }

    protected void requireAny(T... types) {
        if (Arrays.stream(types).noneMatch(t -> t == type)) {
            throw new RuntimeException("Type must be one of " + List.of(types) + " to use this");
        }
    }

    public T getType() {
        return type;
    }
}