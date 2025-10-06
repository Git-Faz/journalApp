package com.journalapp.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "journals")
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message= "Journal Title is Required")
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Journal Content is Required")
    private String content;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'anonymous'")
    private String author = "anonymous";

    @PrePersist
    public void prePersist() {
        if (this.author == null || this.author.trim().isEmpty()) {
            this.author = "anonymous";
        }
    }

    public Journal() {}

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
}
