package com.ruoyi.cms.utils;

public enum BatchType {
    BLOG_TAG(1, "blog_tag"),
    TYPE_TAG(2, "type_tag"),
    BLOG_MENU(3, "blog_menu");
    private final int code;
    private final String name;

    BatchType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
