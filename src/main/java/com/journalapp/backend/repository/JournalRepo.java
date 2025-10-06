package com.journalapp.backend.repository;

import com.journalapp.backend.entity.Journal;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalRepo extends ListCrudRepository <Journal, Long> {

    List<Journal> findByAuthor(String authorName);
}
