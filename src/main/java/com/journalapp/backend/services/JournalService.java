package com.journalapp.backend.services;

import com.journalapp.backend.dto.JournalRequest;
import com.journalapp.backend.entity.User;
import com.journalapp.backend.exceptions.JournalException;
import com.journalapp.backend.exceptions.JournalException;
import com.journalapp.backend.exceptions.UserException;
import com.journalapp.backend.repository.JournalRepo;
import com.journalapp.backend.entity.Journal;
import com.journalapp.backend.repository.UserRepo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    private final JournalRepo journalRepo;
    private final UserRepo userRepo;

    public JournalService(JournalRepo journalRepo, UserRepo userRepo) {
        this.journalRepo = journalRepo;
        this.userRepo = userRepo;
    }

    public List<Journal> getAllJournals() {
        return journalRepo.findAll();
    }

    public List<Journal> getJournalsByUser(Long userId){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new JournalException("User not found with ID: " + userId));

        List<Journal> journals = journalRepo.findByUser(user);

        if (journals.isEmpty()) {
            throw new JournalException("No journals found for user: " + user.getName());
        }
        return journals;
    }

    public Journal getJournalById(Long id) {
        return journalRepo.findById(id)
                .orElseThrow(() -> new JournalException("Journal not found with ID: " + id));
    }


    public Journal createSingleJournal(JournalRequest journalReq) {
        User user = userRepo.findByName(journalReq.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with name: " + journalReq.getUsername()));

        Journal journal = new Journal();
        journal.setTitle(journalReq.getTitle());
        journal.setContent(journalReq.getContent());
        journal.setUser(user);

        try {
            journalRepo.save(journal);
            return journal;
        } catch (Exception e) {
            throw new JournalException("Failed to save journal: " + e.getMessage());
        }

    }

    public List<Journal> createMultipleJournals(List<JournalRequest> journalReqList) {
        if (journalReqList == null || journalReqList.isEmpty()) {
      throw new JournalException("Journal request list cannot be empty");
        }

        String username = journalReqList.getFirst().getUsername();
    User user =
        userRepo
            .findByName(username)
            .orElseThrow(() -> new UserException("User not found with name: " + username));

        List<Journal> journals = journalReqList.stream()
                .map(req -> {
                    Journal j = new Journal();
                    j.setTitle(req.getTitle());
                    j.setContent(req.getContent());
                    j.setUser(user);
                    return j;
                })
                .toList();

        try {
            return journalRepo.saveAll(journals);
        } catch (Exception e) {
            throw new JournalException("Failed to save journals: " + e.getMessage());
        }
    }

    public Journal updateSingleJournal(Long id, JournalRequest journalReq) {
        User user = userRepo.findByName(journalReq.getUsername())
                .orElseThrow(() -> new UserException("User not found with name: " + journalReq.getUsername()));

        Journal journal = journalRepo.findById(id)
                .orElseThrow(() -> new JournalException("Journal not found with id: " + id));

        if (!journal.getUser().getName().equals(user.getName())) {
            throw new AccessDeniedException("You are not authorized to update this journal");
        }

        journal.setTitle(journalReq.getTitle());
        journal.setContent(journalReq.getContent());
        return journalRepo.save(journal);
    }

    public void deleteAllJournals (){
        if(journalRepo.count() > 0) {
            journalRepo.deleteAll();
        }
    }

    public void deleteJournalById(Long id) {
        Journal journal = journalRepo.findById(id)
                .orElseThrow(() -> new JournalException("Journal not found with id: " + id));
        journalRepo.delete(journal);
    }
}
