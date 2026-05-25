package com.hendisantika.springbootckeditor.job;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.CollectionType;
import tools.jackson.databind.type.TypeFactory;
import com.hendisantika.springbootckeditor.model.Song;
import com.hendisantika.springbootckeditor.repository.SongRepo;
import com.hendisantika.springbootckeditor.uploader.FileUploadController;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.ResourcelessJobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.batch.infrastructure.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-ckeditor
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 07/01/18
 * Time: 05.23
 * To change this template use File | Settings | File Templates.
 */

@Configuration
public class UploadJob {
    public static int PRETTY_PRINT_INDENT_FACTOR = 4;
    @Autowired
    FileUploadController uploadController;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SongRepo songDAO;

    // Spring Batch 6 needs a JobRepository and a PlatformTransactionManager.
    // This app has no relational datasource, so use the in-memory, non-persistent
    // resourceless variants instead of the default JDBC ones.
    @Bean
    public JobRepository jobRepository() {
        return new ResourcelessJobRepository();
    }

    @Bean
    public PlatformTransactionManager batchTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    // insert ssg xccdf
                    Path xmlDocPath = uploadController.getCurrentFilePath();
                    String jsonB = processXML2JSON(xmlDocPath);
                    insertToMongo(jsonB);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }


    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    doAQuery();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Job UploadProcessor(JobRepository jobRepository, Step step1, Step step2) {
        return new JobBuilder("UploadProcessor", jobRepository)
                .start(step1)
                .next(step2)
                .build();
    }

    public List<Song> doAQuery() throws IOException {
        Query query = new Query();
        query.addCriteria(Criteria.where("Music.id")
                .is("MUS-1"))
                .fields()
                .include("Music.songs.song");
        String result = mongoTemplate.findOne(query, String.class, "foo");
        Integer indexBegin = result.indexOf("{ \"song\" : ") + 12;
        Integer indexEnd = result.length() - 4;
        String resultFilter = result.substring(indexBegin, indexEnd).trim();
        StringBuilder finalJson = new StringBuilder(resultFilter);
        finalJson.insert(0, "[");
        finalJson.insert(finalJson.length(), "]");
        System.out.println("resulting json:::::: " + finalJson.toString());
        ObjectMapper objectMapper = new ObjectMapper();

        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(
                List.class, Song.class);
        List<Song> songList = objectMapper.readValue(finalJson.toString(), collectionType);
        for (Song songmodel : songList) {
            songmodel.setUpdated(false);
            songDAO.insert(songmodel);
            System.out.println("inserted this songmodel ::: " + songmodel);
        }

        return songList;
    }

    private void insertToMongo(String jsonString) {
        Document doc = Document.parse(jsonString);
        mongoTemplate.insert(doc, "foo");
    }

    private String processXML2JSON(Path xmlDocPath) throws JSONException {
        String XML_STRING = null;
        try {
            XML_STRING = Files.lines(xmlDocPath)
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject xmlJSONObj = XML.toJSONObject(XML_STRING);
        String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
        System.out.println("PRINTING STRING :::::::::::::::::::::" + jsonPrettyPrintString);

        return jsonPrettyPrintString;
    }
}
