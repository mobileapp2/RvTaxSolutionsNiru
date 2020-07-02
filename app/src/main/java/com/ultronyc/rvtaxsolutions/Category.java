package com.ultronyc.rvtaxsolutions;

public class Category {


    private int Thumbnail;
    String activityName;
    int backgroundColor;
    private String categoryId;
    int textColor1;
    int textColor2;
    private String title;

    public Category(String title2, String categoryId2, int thumbnail, int backgroundColor2, int textColor12, int textColor22, String actName) {
        this.title = title2;
        this.categoryId = categoryId2;
        this.Thumbnail = thumbnail;
        this.backgroundColor = backgroundColor2;
        this.textColor1 = textColor12;
        this.textColor2 = textColor22;
        this.activityName = actName;
    }

    public String getTitle() {
        return title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor1() {
        return textColor1;
    }

    public int getTextColor2() {
        return textColor2;
    }

    public String getActivityName() {
        return activityName;
    }
}
