package com.example.myapplication.UserHelper;

public class ConHelper {
    String cnNames,cnPositions,cnPhones,cnWard;

    public ConHelper() { }

    public ConHelper(String cnNames, String cnPositions, String cnPhones, String cnWard) {
        this.cnNames = cnNames;
        this.cnPositions = cnPositions;
        this.cnPhones = cnPhones;
        this.cnWard = cnWard;
    }

    public String getCnWard() {
        return cnWard;
    }

    public void setCnWard(String cnWard) {
        this.cnWard = cnWard;
    }

    public String getCnNames() {
        return cnNames;
    }

    public void setCnNames(String cnNames) {
        this.cnNames = cnNames;
    }

    public String getCnPositions() {
        return cnPositions;
    }

    public void setCnPositions(String cnPositions) {
        this.cnPositions = cnPositions;
    }

    public String getCnPhones() {
        return cnPhones;
    }

    public void setCnPhones(String cnPhones) {
        this.cnPhones = cnPhones;
    }
}
