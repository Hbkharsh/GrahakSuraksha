package com.example.grahaksuraksha.Models;

public class FraudCheckRequest {
    String mobile_number;
    String upi_id;

    public FraudCheckRequest(String mobile_number, String upi_id) {
        this.mobile_number = mobile_number;
        this.upi_id = upi_id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getUpi_id() {
        return upi_id;
    }

    public void setUpi_id(String upi_id) {
        this.upi_id = upi_id;
    }
}
