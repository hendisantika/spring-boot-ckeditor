package com.hendisantika.springbootckeditor.controller;

import com.hendisantika.springbootckeditor.model.UpdatedSong;
import com.hendisantika.springbootckeditor.repository.SongRepo;
import com.hendisantika.springbootckeditor.service.HTMLFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-ckeditor
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 19/09/18
 * Time: 07.03
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/api/")
public class APIController {
    @Autowired
    SongRepo songRepo;

    @Autowired
    UpdatedSongDAO updatedDAO;

    @Autowired
    HTMLFormatter format;

    @GetMapping(value = {"/show/", "/show/{sid}"})
    public ResponseEntity<?> getSong(@RequestParam String sid, Model model) {
        ResponseModel response = new ResponseModel();
        System.out.println("SID :::::" + sid);
        ArrayList<String> musicText = new ArrayList<String>();
        if (sid != null) {
            String sidString = sid;
            SongModel songModel = songDAO.findOne(sidString);
            System.out.println("get status of boolean during get ::::::" + songModel.getUpdated());
            if (songModel.getUpdated() == false) {

                musicText.add(songModel.getArtist());
                musicText.add(songModel.getSongTitle());
                String filterText = format.changeJsonToHTML(musicText);
                System.out.println("formatted song text ::::::::" + filterText);
                response.setData(filterText);

            } else if (songModel.getUpdated() == true) {
                UpdatedSong updated = updatedDAO.findBysid(sidString);
                String text = updated.getHtml();
                System.out.println("getting the updated text ::::::::" + text);
                response.setData(text);
            }

        }

        model.addAttribute("response", response);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = {"/save/", "/save/[sid]"}, consumes = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody
    ResponseModel saveSong(@RequestBody String body, @RequestParam String sid) {
        ResponseModel response = new ResponseModel();
        response.setData(body);
        SongModel oldSong = songDAO.findOne(sid);
        String songTitle = oldSong.getSongTitle();
        String artistName = oldSong.getArtist();
        if (oldSong.getUpdated() == false) {
            UpdatedSong updatedSong = new UpdatedSong();
            updatedSong.setArtist(artistName);
            updatedSong.setSongTitle(songTitle);
            updatedSong.setHtml(body);
            updatedSong.setSid(sid);
            oldSong.setUpdated(true);
            songDAO.save(oldSong);
            updatedDAO.insert(updatedSong);
            System.out.println("get status of boolean during post :::::" + oldSong.getUpdated());
        } else {
            UpdatedSong currentSong = updatedDAO.findBysid(sid);
            currentSong.setHtml(body);
            updatedDAO.save(currentSong);
        }

        return response;
    }


}