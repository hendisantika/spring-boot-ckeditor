package com.hendisantika.springbootckeditor.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-ckeditor
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 06/01/18
 * Time: 07.04
 * To change this template use File | Settings | File Templates.
 */
public class SongModel {
    @Id
    private String id;

    @Indexed
    private String artist;

    @Indexed
    private String songTitle;

    @Indexed
    private Boolean updated;

    @JsonCreator
    public SongModel(
            @JsonProperty("artist") String artist,
            @JsonProperty("song-title") String songTitle) {
        this.artist = artist;
        this.songTitle = songTitle;
    }

    public Boolean getUpdated() {
        return updated;
    }

    public void setUpdated(Boolean updated) {
        this.updated = updated;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", artist=" + artist + ", song-title=" + songTitle + "]";
    }
}
