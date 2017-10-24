package com.betapoint.icoChat;

/**
 * Created by m50571 on 10/23/17.
 */

public class InstantMessageModel {

    private String mMessage;
    private String mAuthor;

    public InstantMessageModel(String _message, String _author) {
        this.mMessage = _message;
        this.mAuthor = _author;
    }

    public InstantMessageModel() {
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }
}
