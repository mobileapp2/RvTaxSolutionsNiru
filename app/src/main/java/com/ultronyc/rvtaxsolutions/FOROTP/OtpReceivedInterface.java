package com.ultronyc.rvtaxsolutions.FOROTP;

public interface OtpReceivedInterface {

    void onOtpReceived(String otp);
    void onOtpTimeout();
}
