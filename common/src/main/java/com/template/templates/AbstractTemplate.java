package com.template.templates;

public abstract class AbstractTemplate {
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    abstract void init();

}
