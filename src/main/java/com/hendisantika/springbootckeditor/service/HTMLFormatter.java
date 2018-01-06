package com.hendisantika.springbootckeditor.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-ckeditor
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 07/01/18
 * Time: 05.22
 * To change this template use File | Settings | File Templates.
 */

@Service
public class HTMLFormatter {
    public String changeJsonToHTML(ArrayList<String> json) {

        String listString = String.join("<br/><br/> ", json);

        return listString;
    }

}
