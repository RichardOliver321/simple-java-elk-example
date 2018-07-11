package com.elkdemo.demo.model;

public class Response<T> {
    private int httpCode;
    private String message;
    private T data;

    public Response() {
    }

    public Response(int httpCode, T data) {
        this.httpCode = httpCode;
        this.data = data;
    }


    public static <T> Response<T> ok(T data, String description) {
        return new Response<>(200, description, data);
    }

    public static <T> Response<T> totallyNotOk(T data, String description) {
        return new Response<>(500, description, data);
    }

    public Response(int httpCode, String message, T data) {
        this.httpCode = httpCode;
        this.message = message;
        this.data = data;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}