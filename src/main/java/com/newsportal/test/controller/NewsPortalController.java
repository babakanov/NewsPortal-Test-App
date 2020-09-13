package com.newsportal.test.controller;

import com.newsportal.test.model.NewsPortal;
import com.newsportal.test.repo.NewsPortalRepo;
import com.newsportal.test.repo.UserRepository;
import com.newsportal.test.service.NewsPortalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class NewsPortalController {

    private NewsPortalService newsPortalService;

    private final UserRepository userRepository;

    private final NewsPortalRepo newsPortalRepo;

    public NewsPortalController(NewsPortalService newsPortalService, UserRepository userRepository, NewsPortalRepo newsPortalRepo) {
        this.newsPortalService = newsPortalService;
        this.userRepository = userRepository;
        this.newsPortalRepo = newsPortalRepo;
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/user/home")
    public String home() {

        return "/user/home";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/user/listNews")
    public String newsListt(@PageableDefault(7) Pageable pageable, Model model) {
        Page<NewsPortal> newsLsit;
        newsLsit = newsPortalService.getAllCurrentDateNews(pageable);
        model.addAttribute("newsList", newsLsit);
        return "/user/newsList";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/user/archiveList")
    public String archiveList(@PageableDefault(7) Pageable pageable, Model model) {
        Page<NewsPortal> newsLsit;
        newsLsit = newsPortalService.getAllArchive(pageable);
        model.addAttribute("newsList", newsLsit);
        return "/user/newsList";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/user/createNews")
    public String createNewNews(Model model) {
        model.addAttribute("newsPortal", new NewsPortal());
        return "/user/createNews";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(value = "/user/createNews")
    public String createNews(@Valid NewsPortal newsPortal, BindingResult bindingResult, @RequestPart("file") final MultipartFile file) throws IOException {
        if (bindingResult.hasErrors()) {
            return "/user/createNews";
        } else {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetails) principal).getUsername();

            newsPortalService.save(userRepository.findByUserName(username), newsPortal, file);
            return "redirect:/user/listNews";
        }
    }

    @PreAuthorize(("hasAnyAuthority('USER')"))
    @GetMapping("/user/newsList/update")
    public String updateUser(@RequestParam(name = "newsId") Long newsId, Model model) throws IOException {
        NewsPortal newsPortal = newsPortalRepo.findById(newsId).get();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        boolean isUsersNews = userRepository.findByUserName(username).getId().equals(newsPortal.getAuthor().getId());

        if (!isUsersNews) {
            return "redirect:/user/listNews?error";
        }

        model.addAttribute("newsPortal", newsPortal);

        return "/user/newsUpdate";
    }

    @PreAuthorize(("hasAnyAuthority('USER')"))
    @PostMapping("/user/newsList/updateSave/{id}")
    public String updateUser(@PathVariable("id") Long id, @Valid @ModelAttribute("newsPortal") NewsPortal newsPortal, MultipartFile file, BindingResult bindingResult) throws IOException {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        if (bindingResult.hasErrors()) {
            return "/user/newsUpdate";
        } else {
            newsPortal.setId(id);
            newsPortalService.save(userRepository.findByUserName(username), newsPortal, file);
        }
        return "redirect:/user/listNews";
    }

    @PreAuthorize("hasAuthority('User')")
    @GetMapping("/user/newsList/delete")
    public String deleteUser(@RequestParam(name = "newsId") Long newsId, Model model) throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        try {
            newsPortalService.delete(userRepository.findByUserName(username), newsId);
        } catch (IOException ex) {
            return "redirect:/user/listNews?error";
        }

        return "redirect:/user/listNews";
    }
}
