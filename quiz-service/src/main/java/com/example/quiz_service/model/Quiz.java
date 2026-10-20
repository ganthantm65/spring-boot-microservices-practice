package com.example.quiz_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity 
@Table (name = "quizzes")
public class Quiz {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer quizId;

    @NotNull 
    private String title;

    private String description;

    @NotNull 
    private boolean active;

    public void setTitle(String title){
        this.title=title;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public void setActive(boolean active){
        this.active=active;
    }

    public Integer getId(){
        return this.quizId;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public boolean isAction(){
        return this.active;
    }
}
