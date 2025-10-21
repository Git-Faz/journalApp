package com.journalapp.backend.controllers;

import com.journalapp.backend.dto.ApiResponse;
import com.journalapp.backend.dto.JournalRequest;
import com.journalapp.backend.dto.JournalResponse;
import com.journalapp.backend.entity.Journal;
import com.journalapp.backend.services.JournalService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/journals")
public class JournalController {

  private final JournalService journalService;

  public JournalController(JournalService journalService) {
    this.journalService = journalService;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<JournalResponse>>> getAll() {
    var journals = journalService.getAllJournals(); // Fetch all Journal entities from DB
    List<JournalResponse> responses =
        journals.stream() // Convert list into a stream for functional operations
            .map(JournalResponse::fromEntity) // Transform each Journal into a JournalResponse DTO
            .toList(); // Collect the transformed elements back into a List
    return ResponseEntity.ok( // Return HTTP 200 with ApiResponse containing message + list
        new ApiResponse<>("Found " + responses.size() + " journals", responses));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<ApiResponse<List<JournalResponse>>> getByUserId(@PathVariable Long userId) {
    var userJournals = journalService.getJournalsByUser(userId);
    List<JournalResponse> responses =
        userJournals.stream().map(JournalResponse::fromEntity).toList();
    return ResponseEntity.ok(
        new ApiResponse<>(
            "Found " + responses.size() + " journals by user ID " + userId, responses));
  }

  @PostMapping("/single")
  public ResponseEntity<ApiResponse<JournalResponse>> createSingleEntry(
      @Valid @RequestBody JournalRequest request) {
    Journal savedJournal = journalService.createSingleJournal(request);
    JournalResponse response = JournalResponse.fromEntity(savedJournal);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>("Journal created successfully", response));
  }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JournalResponse>> getByJournalId(@PathVariable Long id) {
        Journal journal = journalService.getJournalById(id);
        JournalResponse response = JournalResponse.fromEntity(journal);
        return ResponseEntity.ok(new ApiResponse<>("Journal found", response));
    }

    @PostMapping("/multiple")
  public ResponseEntity<ApiResponse<List<JournalResponse>>> createMultipleJournals(
      @Valid @RequestBody List<JournalRequest> journalRequests) {

    List<Journal> savedJournals = journalService.createMultipleJournals(journalRequests);

    // Convert List<Journal> to List<JournalResponse>
    List<JournalResponse> responseList =
        savedJournals.stream()
            .map(
                journal ->
                    new JournalResponse(
                        journal.getId(),
                        journal.getTitle(),
                        journal.getContent(),
                        journal.getUser().getName()))
            .toList();

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>("Journals created successfully", responseList));
  }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<JournalResponse>> updateSingleEntry(
            @PathVariable Long id, @Valid @RequestBody JournalRequest journal) {
        Journal updatedJournal = journalService.updateSingleJournal(id, journal);
        JournalResponse response = JournalResponse.fromEntity(updatedJournal);
        return ResponseEntity.ok(new ApiResponse<>("Journal updated successfully", response));
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<ApiResponse<String>> deleteAll() {
        journalService.deleteAllJournals();
        return ResponseEntity.ok(
                new ApiResponse<>("Deleted all journals successfully")
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteById(@PathVariable Long id) {
        journalService.deleteJournalById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<>("Journal deleted successfully"));
    }

}



