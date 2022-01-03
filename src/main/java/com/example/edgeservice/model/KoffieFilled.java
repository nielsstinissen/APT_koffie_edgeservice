package com.example.edgeservice.model;

public class KoffieFilled {
    private Koffie koffie;
    private Boon boon;

    public Koffie getKoffie() {
        return koffie;
    }

    public Boon getBoon() {
        return boon;
    }

    public void setKoffie(Koffie koffie) {
        this.koffie = koffie;
    }

    public void setBoon(Boon boon) {
        this.boon = boon;
    }

    public KoffieFilled(Koffie koffie, Boon boon) {
        this.koffie = koffie;
        this.boon = boon;
    }
}
