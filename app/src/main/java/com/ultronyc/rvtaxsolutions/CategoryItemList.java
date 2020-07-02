package com.ultronyc.rvtaxsolutions;

public class CategoryItemList {

    private String brand;
    private String group;
    private String itemName;
    private String mrp;
    private String rate;
    private String tax;
    private String type;
    private String unit;

    public CategoryItemList(String itemName2, String brand2, String unit2, String tax2, String type2, String group2, String rate2, String mrp2) {
        this.itemName = itemName2;
        this.brand = brand2;
        this.unit = unit2;
        this.tax = tax2;
        this.type = type2;
        this.group = group2;
        this.rate = rate2;
        this.mrp = mrp2;
    }

    public String getItemName() {
        return itemName;
    }

    public String getBrand() {
        return brand;
    }

    public String getUnit() {
        return unit;
    }

    public String getTax() {
        return tax;
    }

    public String getType() {
        return type;
    }

    public String getGroup() {
        return group;
    }

    public String getRate() {
        return rate;
    }

    public String getMrp() {
        return mrp;
    }


}
