package ru.otus.cinemaapp.model;

public class Film {

    private Long id;
    private String title;
    private String description;
    private int imageResourceId;

    public Film(Long id, String title, String description, int imageResourceId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageResourceId = imageResourceId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
