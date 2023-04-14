package com.example.grahaksuraksha.Models;

import java.util.Base64;

public class ReportModel {
    User user;
    String fraud_type;
    String reported_entity;
    Base64 supporting_document=null; //No required
    String description;

    public ReportModel(User user, String fraud_type, String reported_entity, String description) {
        this.user = user;
        this.fraud_type = fraud_type;
        this.reported_entity = reported_entity;
        this.description = description;
    }

    public Base64 getSupporting_document() {
        return supporting_document;
    }

    public void setSupporting_document(Base64 supporting_document) {
        this.supporting_document = supporting_document;
    }
}
