package com.ultronyc.rvtaxsolutions;

public class CategoryCustList {

    String custAddress;
    String custAnniversaryDate;
    String custBirthDate;
    String custEmail;
    String custFullName;
    String custMobile;

    public CategoryCustList(String custFullName2, String custMobile2, String custEmail2, String custAddress2, String custBirthDate2, String custAnniversaryDate2) {
        this.custFullName = custFullName2;
        this.custMobile = custMobile2;
        this.custEmail = custEmail2;
        this.custAddress = custAddress2;
        this.custBirthDate = custBirthDate2;
        this.custAnniversaryDate = custAnniversaryDate2;
    }

    public String getCustFullName() {
        return custFullName;
    }

    public String getCustMobile() {
        return custMobile;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public String getCustBirthDate() {
        return custBirthDate;
    }

    public String getCustAnniversaryDate() {
        return custAnniversaryDate;
    }

}
