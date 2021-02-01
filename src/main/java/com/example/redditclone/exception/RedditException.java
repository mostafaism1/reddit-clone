package com.example.redditclone.exception;

public class RedditException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RedditException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public RedditException(String exMessage) {
        super(exMessage);
    }
}