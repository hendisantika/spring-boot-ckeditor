package com.hendisantika.springbootckeditor.controller;

import com.hendisantika.springbootckeditor.model.Music;
import com.hendisantika.springbootckeditor.model.Pager;
import com.hendisantika.springbootckeditor.model.Song;
import com.hendisantika.springbootckeditor.repository.MusicRepo;
import com.hendisantika.springbootckeditor.repository.SongRepo;
import com.hendisantika.springbootckeditor.uploader.FileUploadController;
import com.hendisantika.springbootckeditor.uploader.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-ckeditor
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 19/09/18
 * Time: 07.00
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class WebController {

    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = {5, 10, 50, 100};
    @Autowired
    private final StorageService storageService;
    @Autowired
    MusicRepo musicRepo;
    @Autowired
    SongRepo songRepo;

    public WebController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public ModelAndView homepage(@RequestParam("pageSize") Optional<Integer> pageSize, @RequestParam("page") Optional<Integer> page) {

        ModelAndView modelAndView = new ModelAndView("uploadForm");
        //
        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<Music> Musiclist = musicRepo.findAll(new PageRequest(evalPage, evalPageSize));
        System.out.println("client list get total pages" + Musiclist.getTotalPages() + "client list get number " + Musiclist.getNumber());
        Pager pager = new Pager(Musiclist.getTotalPages(), Musiclist.getNumber(), BUTTONS_TO_SHOW);

        // add clientmodel
        modelAndView.addObject("Musiclist", Musiclist);
        // evaluate page size
        modelAndView.addObject("selectedPageSize", evalPageSize);
        // add page sizes
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        // add pager
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("files", storageService.loadAll()
                .map(path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "serveFile", path.getFileName()
                        .toString())
                        .build()
                        .toString())
                .collect(Collectors.toList()));
        return modelAndView;

    }

    @RequestMapping(value = {"/view", "/view/", "/view/{pageSize}{page}/", "/view/{pageSize}", "/view/{pageSize}{page}/"}, method = RequestMethod.GET)
    public ModelAndView viewdoc(@RequestParam("pageSize") Optional<Integer> pageSize, @RequestParam("page") Optional<Integer> page, @RequestParam("rid") Optional<String> rid) {

        ModelAndView modelAndView = new ModelAndView();

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;


        Page<Song> songList = songRepo.findAll(new PageRequest(evalPage, evalPageSize));
        System.out.println("PAGE RULE LIST :::::::: " + songList);

        Pager pager = new Pager(songList.getTotalPages(), songList.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("songList", songList);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        // add page sizes
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        // add pager
        modelAndView.addObject("pager", pager);
        // modelAndView.addObject("rule", ruleText);
        modelAndView.setViewName("view");

        return modelAndView;

    }

}