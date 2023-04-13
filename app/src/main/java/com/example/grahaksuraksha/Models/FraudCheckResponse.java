package com.example.grahaksuraksha.Models;

public class FraudCheckResponse {
    boolean is_gov_verified ;
    boolean is_fraud;
    int number_of_userReported;

    public FraudCheckResponse(boolean is_gov_verified, boolean is_fraud, int number_of_userReported) {
        this.is_gov_verified = is_gov_verified;
        this.is_fraud = is_fraud;
        this.number_of_userReported = number_of_userReported;
    }

    public boolean isIs_gov_verified() {
        return is_gov_verified;
    }

    public boolean isIs_fraud() {
        return is_fraud;
    }

    public int getNumber_of_userReported() {
        return number_of_userReported;
    }
//Is gov verified - true then show red card
    //ig gov verified false ,&&& is fraud true check count = and show
    // else if(is fraud false do nothing
}
