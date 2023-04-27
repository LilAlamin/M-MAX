package com.example.ta_sinarmas;

public class ItemMovie {
    private String title, overview, posterPath;
    public ItemMovie setTitle(String title) {
        this.title = title;
        return this;
    }
    public ItemMovie setOverview(String overview) {
        this.overview = overview;
        return this;
    }
    public ItemMovie setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }
    public String getTitle() {
        return title;
    }
    public String getOverview() {
        return overview;
    }
    public String getPosterPath() {
        return posterPath;
    }
}
