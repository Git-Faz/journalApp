package com.journalapp.backend.dto;

import com.journalapp.backend.entity.Journal;

public class JournalResponse {
    private Long id;
    private String title;
    private String content;
    private String user;

    public JournalResponse() {}

    public JournalResponse(Long id, String title, String content, String user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static JournalResponse fromEntity(Journal journal) {
        String username = null;
        if (journal.getUser() != null) {
            username = journal.getUser().getName();
        }
        return new JournalResponse(journal.getId(), journal.getTitle(), journal.getContent(), username);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
