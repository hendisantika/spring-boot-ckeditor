package com.hendisantika.springbootckeditor.repository;

import com.hendisantika.springbootckeditor.model.Music;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-ckeditor
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 07/01/18
 * Time: 05.19
 * To change this template use File | Settings | File Templates.
 */

@Repository
public interface MusicRepo extends MongoRepository<Music, String> {
    Music findTop1ByOrderByIdDesc();
}
