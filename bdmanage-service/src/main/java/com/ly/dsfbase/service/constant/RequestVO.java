package com.ly.dsfbase.service.constant;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cody on 2015/11/11.
 */
public class RequestVO<T> {

    @SerializedName("url")
    private String URL;
    @SerializedName("method")
    private String method;

    @SerializedName("token")
    private String token;

    @SerializedName("useruuid")
    private String userId;

    @SerializedName("username")
    private String userName;

    @SerializedName("offset")
    private String offset;

    @SerializedName("data")
    private T data;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
