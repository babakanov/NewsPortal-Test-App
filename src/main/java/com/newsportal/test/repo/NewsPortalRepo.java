package com.newsportal.test.repo;

import com.newsportal.test.model.NewsPortal;
import com.newsportal.test.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NewsPortalRepo extends JpaRepository<NewsPortal, Long> {
    @Query(value = "select * from news_portal where date_of_create = current_date ", nativeQuery = true)
    Page<NewsPortal> getAllCurrentDateNews(Pageable pageable);


    @Query(value = "select * from news_portal where date_of_create <> current_date ", nativeQuery = true)
    Page<NewsPortal> getAllArchive(Pageable pageable);

    void deleteById(Long id);
}
