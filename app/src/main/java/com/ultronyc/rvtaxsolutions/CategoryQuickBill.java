package com.ultronyc.rvtaxsolutions;

public class CategoryQuickBill {

    String amount;
    String count;
    String imageUrl;
    String itemBrand;
    String itemGroup;
    String itemMRP;
    String itemName;
    String itemRate;
    String itemTax;
    String itemType;
    String itemUnit;

    public CategoryQuickBill(String itemName2, String itemType2, String itemBrand2, String itemGroup2, String itemUnit2, String itemRate2, String itemTax2, String itemMRP2, String count2, String imageUrl2, String amount2) {
        this.itemName = itemName2;
        this.itemType = itemType2;
        this.itemBrand = itemBrand2;
        this.itemGroup = itemGroup2;
        this.itemUnit = itemUnit2;
        this.itemRate = itemRate2;
        this.itemTax = itemTax2;
        this.itemMRP = itemMRP2;
        this.count = count2;
        this.imageUrl = imageUrl2;
        this.amount = amount2;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public String getItemRate() {
        return itemRate;
    }

    public String getItemTax() {
        return itemTax;
    }

    public String getItemMRP() {
        return itemMRP;
    }

    public String getCount() {
        return count;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAmount() {
        return amount;
    }

    public void setItemName(String itemName2) {
        itemName = itemName2;
    }

    public void setItemType(String itemType2) {
        itemType = itemType2;
    }

    public void setItemBrand(String itemBrand2) {
        itemBrand = itemBrand2;
    }

    public void setItemGroup(String itemGroup2) {
        itemGroup = itemGroup2;
    }

    public void setItemUnit(String itemUnit2) {
        itemUnit = itemUnit2;
    }

    public void setItemRate(String itemRate2) {
        itemRate = itemRate2;
    }

    public void setItemTax(String itemTax2) {
        itemTax = itemTax2;
    }

    public void setItemMRP(String itemMRP2) {
        itemMRP = itemMRP2;
    }

    public void setCount(String count2) {
        count = count2;
    }

    public void setImageUrl(String imageUrl2) {
        imageUrl = imageUrl2;
    }

    public void setAmount(String amount2) {
        amount = amount2;
    }


}
