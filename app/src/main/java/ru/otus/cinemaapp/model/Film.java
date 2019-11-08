package ru.otus.cinemaapp.model;

public class Film {

    private static long counter;

    private Long id;
    private String title;
    private String description;
    private int imageResourceId;
    private boolean isLiked;
    private boolean isRemarkable;

    public Film(Long id, String title, String description, int imageResourceId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    public static Long generateId() {
        return counter++;
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

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public static long getCounter() {
        return counter;
    }

    public static void setCounter(long counter) {
        Film.counter = counter;
    }

    public boolean isRemarkable() {
        return isRemarkable;
    }

    public void setRemarkable(boolean remarkable) {
        isRemarkable = remarkable;
    }
}
