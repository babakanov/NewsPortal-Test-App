package com.newsportal.test.service;

import com.newsportal.test.model.NewsPortal;
import com.newsportal.test.model.User;
import com.newsportal.test.model.UtilBase64Image;
import com.newsportal.test.repo.NewsPortalRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class NewsPortalService {
    private final NewsPortalRepo newsPortalRepo;

    public NewsPortalService(NewsPortalRepo newsPortalRepo) {
        this.newsPortalRepo = newsPortalRepo;
    }


    public NewsPortal save(User user, NewsPortal newsPortal, MultipartFile file) throws IOException {
        if (file != null && file.getContentType().contains("image")) {
            newsPortal.setDateOfCreate(LocalDate.now());
            newsPortal.setImage(UtilBase64Image.encoder(file));
            newsPortal.setAuthor(user);
        }
        return newsPortalRepo.save(newsPortal);
    }

    public void delete(User user, Long newsPortalId) throws IOException {
        NewsPortal newsPortal = newsPortalRepo.findById(newsPortalId).get();
        if (user.getId().equals(newsPortal.getAuthor().getId())) {
            newsPortalRepo.delete(newsPortal);
        } else {
            throw new IOException("It's not your News Portal");
        }
    }

    public Page<NewsPortal> getAllArchive(Pageable pageable) {
        return newsPortalRepo.getAllArchive(pageable);
    }

    public Page<NewsPortal> getAllCurrentDateNews(Pageable pageable) {
        return newsPortalRepo.getAllCurrentDateNews(pageable);
    }
}
