package com.corona.coronavirustracker.models;

public class LocationStats {

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLatestTotalCases() {
        return latestTotalCases;
    }

    public void setLatestTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }

    public int getDiffFromPrevDay() {
        return diffFromPrevDay;
    }

    public void setDiffFromPrevDay(int diffFromPrevDay) {
        this.diffFromPrevDay = diffFromPrevDay;
    }

    private String country;
    private int latestTotalCases;
    private int diffFromPrevDay;

    public int getLatestDeathCases() {
        return latestDeathCases;
    }

    public void setLatestDeathCases(int latestDeathCases) {
        this.latestDeathCases = latestDeathCases;
    }

    public int getDeathDiffFromPrevDay() {
        return deathDiffFromPrevDay;
    }

    public void setDeathDiffFromPrevDay(int deathDiffFromPrevDay) {
        this.deathDiffFromPrevDay = deathDiffFromPrevDay;
    }

    private int latestDeathCases;
    private int deathDiffFromPrevDay;
}
