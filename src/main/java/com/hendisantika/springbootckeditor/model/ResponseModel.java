package com.hendisantika.springbootckeditor.model;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-ckeditor
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 06/01/18
 * Time: 07.03
 * To change this template use File | Settings | File Templates.
 */
public class ResponseModel {
    private String data;

    public ResponseModel() {

    }

    public ResponseModel(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
