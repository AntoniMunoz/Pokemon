package com.example.antonio.pokemon.models;

public class Pokemon {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String name;
    private String url;

    public int getNum() {
        String[] urlParte = url.split("/");
        return Integer.parseInt(urlParte[urlParte.length-1]);
    }

    public void setNum(int num) {
        this.num = num;
    }

    private int num;
}
