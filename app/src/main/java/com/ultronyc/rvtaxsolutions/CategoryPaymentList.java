package com.ultronyc.rvtaxsolutions;

public class CategoryPaymentList {


    String name;
    String date;
    String amt;
    String paymentid;
    String transactionid;
    String processid;
    String status;

    public CategoryPaymentList(String name2, String date2, String amt2, String paymentid2,String transactionid2,String processid2,String status2) {
        this.name = name2;
        this.date = date2;
        this.amt = amt2;
        this.paymentid = paymentid2;
        this.transactionid = transactionid2;
        this.processid = processid2;
        this.status = status2;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getAmt() {
        return amt;
    }

    public String getPaymentid() {
        return paymentid;
    }
    public String getStatus() {
        return status;
    }
    public String getProcessid() {
        return processid;
    }
    public String getTransactionid() {
        return transactionid;
    }

}
