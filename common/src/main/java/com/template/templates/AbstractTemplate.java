package com.template.templates;

public abstract class AbstractTemplate {
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    abstract void init();

}
