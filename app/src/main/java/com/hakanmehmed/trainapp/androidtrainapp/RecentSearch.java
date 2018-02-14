package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 14/02/2018.
 */

public class RecentSearch {
    private String from;
    private String to;

    public RecentSearch(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "RecentSearch{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
