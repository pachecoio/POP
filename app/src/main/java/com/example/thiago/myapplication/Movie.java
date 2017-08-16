package com.example.thiago.myapplication;

/**
 * Created by thiagop on 8/16/17.
 */

public class Movie {

    String VOTE_COUNT;
    String ID;
    String VIDEO;
    String VOTE_AVERAGE;
    String TITLE;
    String POPULARITY;
    String POSTER_PATH;
    String ORIGINAL_LANGUAGE;
    String ORIGINAL_TITLE;
    Boolean isAdult;
    String OVERVIEW;
    String RELEASE_DATE;

    public Movie(String VOTE_COUNT,
                 String ID,
                 String VIDEO,
                 String VOTE_AVERAGE,
                 String TITLE,
                 String POPULARITY,
                 String POSTER_PATH,
                 String ORIGINAL_LANGUAGE,
                 String ORIGINAL_TITLE,
                 Boolean isAdult,
                 String OVERVIEW,
                 String RELEASE_DATE) {

        this.VOTE_COUNT = VOTE_COUNT;
        this.ID = ID;
        this.VIDEO = VIDEO;
        this.VOTE_AVERAGE = VOTE_AVERAGE;
        this.TITLE = TITLE;
        this.POPULARITY = POPULARITY;
        this.POSTER_PATH = POSTER_PATH;
        this.ORIGINAL_LANGUAGE = ORIGINAL_LANGUAGE;
        this.ORIGINAL_TITLE = ORIGINAL_TITLE;
        this.isAdult = isAdult;
        this.OVERVIEW = OVERVIEW;
        this.RELEASE_DATE = RELEASE_DATE;
    }

    public String getVOTE_COUNT() {
        return VOTE_COUNT;
    }

    public void setVOTE_COUNT(String VOTE_COUNT) {
        this.VOTE_COUNT = VOTE_COUNT;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getVIDEO() {
        return VIDEO;
    }

    public void setVIDEO(String VIDEO) {
        this.VIDEO = VIDEO;
    }

    public String getVOTE_AVERAGE() {
        return VOTE_AVERAGE;
    }

    public void setVOTE_AVERAGE(String VOTE_AVERAGE) {
        this.VOTE_AVERAGE = VOTE_AVERAGE;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getPOPULARITY() {
        return POPULARITY;
    }

    public void setPOPULARITY(String POPULARITY) {
        this.POPULARITY = POPULARITY;
    }

    public String getPOSTER_PATH() {
        return POSTER_PATH;
    }

    public void setPOSTER_PATH(String POSTER_PATH) {
        this.POSTER_PATH = POSTER_PATH;
    }

    public String getORIGINAL_LANGUAGE() {
        return ORIGINAL_LANGUAGE;
    }

    public void setORIGINAL_LANGUAGE(String ORIGINAL_LANGUAGE) {
        this.ORIGINAL_LANGUAGE = ORIGINAL_LANGUAGE;
    }

    public String getORIGINAL_TITLE() {
        return ORIGINAL_TITLE;
    }

    public void setORIGINAL_TITLE(String ORIGINAL_TITLE) {
        this.ORIGINAL_TITLE = ORIGINAL_TITLE;
    }

    public Boolean getAdult() {
        return isAdult;
    }

    public void setAdult(Boolean adult) {
        isAdult = adult;
    }

    public String getOVERVIEW() {
        return OVERVIEW;
    }

    public void setOVERVIEW(String OVERVIEW) {
        this.OVERVIEW = OVERVIEW;
    }

    public String getRELEASE_DATE() {
        return RELEASE_DATE;
    }

    public void setRELEASE_DATE(String RELEASE_DATE) {
        this.RELEASE_DATE = RELEASE_DATE;
    }
}
