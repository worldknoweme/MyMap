package com.cx.mymap.model;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by wang-jl on 2018/3/15.
 */

public class History extends BmobObject {

    private String username;

    private String type;

    private String des;

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    private Date createAt;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
