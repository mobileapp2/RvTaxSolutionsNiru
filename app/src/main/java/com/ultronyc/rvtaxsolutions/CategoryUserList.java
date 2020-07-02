package com.ultronyc.rvtaxsolutions;

public class CategoryUserList {


    String address;
    String email;
    String mobile;
    String name;

    public CategoryUserList(String name2, String email2, String mobile2, String address2) {
        this.name = name2;
        this.email = email2;
        this.mobile = mobile2;
        this.address = address2;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

}
