package com.project.pytopath.models;

public class Disease {
    final int id;
    final String diseasEn, diseaseMr, diseaseInfo, diseaseCure;

    public Disease(int id, String diseasEn, String diseaseMr, String diseaseInfo, String diseaseCure) {
        this.id = id;
        this.diseasEn = diseasEn;
        this.diseaseMr = diseaseMr;
        this.diseaseInfo = diseaseInfo;
        this.diseaseCure = diseaseCure;
    }

    public int getId() {
        return id;
    }

    public String getDiseasEn() {
        return diseasEn;
    }

    public String getDiseaseCure() {
        return diseaseCure;
    }

    public String getDiseaseInfo() {
        return diseaseInfo;
    }

    public String getDiseaseMr() {
        return diseaseMr;
    }
}
