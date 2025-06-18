package com.donghyukki.springjava.support.security.support.common.result;


public abstract class Result<T, E extends Exception> {

    public static class Success<T, E extends Exception> extends Result<T, E> {
        private final T value;

        public Success(T value) {
            this.value = value;
        }

        @Override
        public T getOrThrow() {
            return value;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }
    }

    public static class Failure<T, E extends Exception> extends Result<T, E> {
        private final E error;

        public Failure(E error) {
            this.error = error;
        }

        @Override
        public T getOrThrow() throws E {
            throw error;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        public E getError() {
            return error;
        }
    }

    public static <T, E extends Exception> Result<T, E> success(T value) {
        return new Success<>(value);
    }

    public static <T, E extends Exception> Result<T, E> failure(E error) {
        return new Failure<>(error);
    }

    public abstract T getOrThrow() throws E;

    public abstract boolean isSuccess();
}
