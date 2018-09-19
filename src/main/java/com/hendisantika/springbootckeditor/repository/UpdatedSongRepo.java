package com.hendisantika.springbootckeditor.repository;

import com.hendisantika.springbootckeditor.model.UpdatedSong;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-ckeditor
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 19/09/18
 * Time: 07.04
 * To change this template use File | Settings | File Templates.
 */
public interface UpdatedSongRepo extends MongoRepository<UpdatedSong, String> {
    UpdatedSong findBysid(String sid);
}
