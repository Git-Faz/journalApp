package com.journalapp.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Username is required")
    @JoinColumn(name = "user_id")
    private User user;

    /*@PrePersist
    public void prePersist() {
        if (this.author == null || this.author.trim().isEmpty()) {
            this.author = "anonymous";
        }
    }*/

    public Journal() {}


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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
