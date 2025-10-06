package com.journalapp.backend.controllers;

import com.journalapp.backend.dto.ApiResponse;
import com.journalapp.backend.entity.Journal;
import com.journalapp.backend.repository.JournalRepo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journal")
public class JournalController {

    private final JournalRepo journalRepo;

    public JournalController(JournalRepo journalRepo) {
        this.journalRepo = journalRepo;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Journal>>> getAll() {
        List<Journal> journals = journalRepo.findAll();
        return ResponseEntity.ok(new ApiResponse<>("Found " + journals.size() + " journals", journals));
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<ApiResponse<List<Journal>>> getByAuthor(@PathVariable String author) {
        List<Journal> authorJournals = journalRepo.findByAuthor(author);
        if(authorJournals.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("No journals found for author: " + author));
        }
        return ResponseEntity.ok(new ApiResponse<>("Found " + authorJournals.size() + " journals by " + author, authorJournals));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Journal>> getById(@PathVariable Long id) {
        return journalRepo.findById(id)
                .map(journal -> ResponseEntity.ok(new ApiResponse<>("Journal found", journal)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Journal with id " + id + " not found")));
    }

    @PostMapping("/single")
    public ResponseEntity<ApiResponse<Journal>> createSingleEntry(@Valid @RequestBody Journal journal) {
        journal.setId(null);
        Journal savedJournal = journalRepo.save(journal);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Journal created successfully", savedJournal));
    }

    @PostMapping("/multiple")
    public ResponseEntity<ApiResponse<List<Journal>>>  createMultipleEntries(@Valid @RequestBody List<Journal> journals) {
        for(Journal journal : journals){
            journal.setId(null);
            journalRepo.save(journal);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Journals created successfully", journals));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Journal>> updateSingleEntry(@PathVariable Long id, @Valid @RequestBody Journal journal) {
        if (journalRepo.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Journal with id " + id + " does not exist"));
        }
        journal.setId(id);
        Journal updatedJournal = journalRepo.save(journal);
        return ResponseEntity.ok(new ApiResponse<>("Journal with id " + id + " updated", updatedJournal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteById(@PathVariable Long id) {
        if (journalRepo.findById(id).isPresent()) {
            journalRepo.deleteById(id);
            return ResponseEntity.ok(new ApiResponse<>("Journal with id " + id + " deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>("Journal with id " + id + " not found"));
    }
}


