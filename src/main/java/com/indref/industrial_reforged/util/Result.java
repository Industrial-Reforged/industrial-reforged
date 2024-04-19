package com.indref.industrial_reforged.util;

import java.util.Optional;

public class Result<T, E extends Throwable> {
    private final T okValue;
    private final E errValue;

    private Result(T okValue, E errValue) {
        this.okValue = okValue;
        this.errValue = errValue;
    }

    public static <T, E extends Throwable> Result<T, E> ok(T value) {
        return new Result<>(value, null);
    }

    public static <T, E extends Throwable> Result<T, E> err(E error) {
        return new Result<>(null, error);
    }

    public boolean isOk() {
        return this.okValue != null;
    }

    public boolean isErr() {
        return this.errValue != null;
    }

    public T unwrap() {
        if (isErr())
            throw new IllegalStateException("Called unwrap() on an Err result");

        return okValue;
    }

    public E unwrapErr() {
        if (isOk())
            throw new IllegalStateException("Called unwrapErr() on an Ok result");

        return errValue;
    }

    public <N> Result<N, E> map(MapResult<T, N> map) {
        if (isOk())
            return ok(map.map(this.okValue));

        return err(this.errValue);
    }

    public <N extends Throwable> Result<T, N> mapErr(MapResult<E, N> map) {
        if (isErr())
            return err(map.map(this.errValue));

        return ok(this.okValue);
    }

    public T expect(String msg) {
        if (isErr())
            throw new IllegalStateException(msg);
        return this.okValue;
    }

    public <U> Result<U, E> and(Result<U, E> other) {
        if (isOk())
            return other;
        return err(this.errValue);
    }

    public <F extends Throwable> Result<T, F> or(Result<T, F> other) {
        if (isOk())
            return ok(this.okValue);
        return other;
    }

    public Optional<T> okToOpt() {
        if (isOk())
            return Optional.of(this.okValue);
        return Optional.empty();
    }

    public Optional<E> errToOpt() {
        if (isErr())
            return Optional.of(this.errValue);
        return Optional.empty();
    }

    @FunctionalInterface
    public interface MapResult<T, N> {
        N map(T val);
    }
}