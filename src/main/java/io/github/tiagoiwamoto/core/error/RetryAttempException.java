package io.github.tiagoiwamoto.core.error;

public class RetryAttempException extends RuntimeException {

    public RetryAttempException() {
        super("Retry attempt failed. Please try again later.");
    }
}
