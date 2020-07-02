package com.ultronyc.rvtaxsolutions;

public class CategorySettings {

    int imageRes;
    String name;
    String isChecked;

    public CategorySettings(int imageRes, String name, String isChecked) {
        this.imageRes = imageRes;
        this.name = name;
        this.isChecked = isChecked;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getName() {
        return name;
    }

    public String getIsChecked() {
        return isChecked;
    }


    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }
}
