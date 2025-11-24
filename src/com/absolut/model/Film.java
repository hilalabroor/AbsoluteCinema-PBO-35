package com.absolut.model;

public class Film {
    private int id;
    private String title;
    private String genre;
    private double rating;
    private String synopsis;
    private String posterPath;
    private String trailerGifPath;
    private String audioPath;

    // Constructor
    public Film(int id, String title, String genre, double rating, String synopsis, 
                String posterPath, String trailerGifPath, String audioPath) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.synopsis = synopsis;
        this.posterPath = posterPath;
        this.trailerGifPath = trailerGifPath;
        this.audioPath = audioPath;
    }

    // Getters (Penting untuk mengambil data nanti)
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public double getRating() { return rating; }
    public String getSynopsis() { return synopsis; }
    public String getPosterPath() { return posterPath; }
    public String getTrailerGifPath() { return trailerGifPath; }
    public String getAudioPath() { return audioPath; }
}