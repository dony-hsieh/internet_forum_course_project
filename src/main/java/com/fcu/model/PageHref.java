package com.fcu.model;

public class PageHref {
    private int number;
    private String href;

    public PageHref() {

    }

    public PageHref(int number, String href) {
        this.number = number;
        this.href = href;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
