package com.ys.model;

/**
 * @author smile
 * @version 1.0
 * @date 2021/3/21 下午8:01
 * @describe $
 */
public class PermissionDto {


    private int id;

    private String code;

    private String descprition;

    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescprition() {
        return descprition;
    }

    public void setDescprition(String descprition) {
        this.descprition = descprition;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
