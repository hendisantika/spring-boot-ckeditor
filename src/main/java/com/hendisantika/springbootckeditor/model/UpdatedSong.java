package com.hendisantika.springbootckeditor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-ckeditor
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 06/01/18
 * Time: 07.05
 * To change this template use File | Settings | File Templates.
 */
public class UpdatedSong {
    @Id
    private String id;

    @Indexed
    private String artist;

    @Indexed
    private String songTitle;

    @Indexed
    private String html;

    @Indexed
    private String sid;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
