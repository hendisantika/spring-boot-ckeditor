package com.hendisantika.springbootckeditor.repository;

import com.hendisantika.springbootckeditor.model.Song;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-ckeditor
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 07/01/18
 * Time: 05.21
 * To change this template use File | Settings | File Templates.
 */

@Repository
public interface SongRepo extends MongoRepository<Song, String> {
}
