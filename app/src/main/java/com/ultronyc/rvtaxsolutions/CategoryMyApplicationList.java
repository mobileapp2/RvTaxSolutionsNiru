package com.ultronyc.rvtaxsolutions;

public class CategoryMyApplicationList {


    String name;
    String date;
    String amt;
    String paymentid;
    String transactionid;
    String processid;
    String status;
    String replay_msg;
    String replay_doc;
    String replay_doc_type;

    public CategoryMyApplicationList(String name2, String date2, String amt2, String paymentid2, String transactionid2, String processid2, String status2,String replay_msg2,String replay_doc2,String replay_doc_type2) {
        this.name = name2;
        this.date = date2;
        this.amt = amt2;
        this.paymentid = paymentid2;
        this.transactionid = transactionid2;
        this.processid = processid2;
        this.status = status2;
        this.replay_msg = replay_msg2;
        this.replay_doc = replay_doc2;
        this.replay_doc_type = replay_doc_type2;
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
    public String getReplayMsg() {
        return replay_msg;
    }
    public String getReplayDoc() {
        return replay_doc;
    }public String getReplayDocType() {
        return replay_doc_type;
    }

}
