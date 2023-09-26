package com.example.covidscanner;

import java.util.Date;

public class Screening
{
    private double temperature;
    private Date created;
    private String code;
    private boolean highRiskCountry;
    private boolean inContactWithCovidPeople;
    private boolean haveSymptoms;
    private String schoolEmail;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isHighRiskCountry() {
        return highRiskCountry;
    }

    public void setHighRiskCountry(boolean highRiskCountry) {
        this.highRiskCountry = highRiskCountry;
    }

    public boolean isInContactWithCovidPeople() {
        return inContactWithCovidPeople;
    }

    public void setInContactWithCovidPeople(boolean inContactWithCovidPeople) {
        this.inContactWithCovidPeople = inContactWithCovidPeople;
    }

    public boolean isHaveSymptoms() {
        return haveSymptoms;
    }

    public void setHaveSymptoms(boolean haveSymptoms) {
        this.haveSymptoms = haveSymptoms;
    }

    public String getSchoolEmail() {
        return schoolEmail;
    }

    public void setSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
    }
}
