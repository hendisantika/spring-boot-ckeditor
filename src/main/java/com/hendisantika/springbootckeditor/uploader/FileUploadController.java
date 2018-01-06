package com.hendisantika.springbootckeditor.uploader;

import com.hendisantika.springbootckeditor.job.UploadJob;
import com.hendisantika.springbootckeditor.repository.MusicRepo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-ckeditor
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 07/01/18
 * Time: 05.26
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class FileUploadController {
    @Autowired
    private final StorageService storageService;
    public Path currentFilePath;
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    MusicRepo musicDAO;
    @Autowired
    private UploadJob uploadJob;

    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;

    }

    public Path getCurrentFilePath() {
        return currentFilePath;
    }

    public void setCurrentFilePath(Path currentFilePath) {
        this.currentFilePath = currentFilePath;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/")
    public String postTheFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
            throws IOException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        // store the file to server
        storageService.store(file);
        // convert multipartfile to file
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        String filepath = convFile.getAbsolutePath();
        Path xmlPathbb = Paths.get(filepath);
        // set current file path
        setCurrentFilePath(xmlPathbb);
        System.out.println("xmlPathbb:::::" + xmlPathbb);
        //

        Job job = uploadJob.UploadProcessor();
        jobLauncher.run(job, new JobParameters());

        return "uploadForm";
    }

}
