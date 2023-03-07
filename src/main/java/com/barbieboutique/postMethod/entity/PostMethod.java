package com.barbieboutique.postMethod.entity;

public abstract class PostMethod {
    private String name;

    public PostMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
