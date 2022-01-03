package com.example.edgeservice.model;

public class Gerechtenfilled {
    private Gerecht gerecht;
    private Koffie koffie;

    public Gerechtenfilled(Gerecht gerecht, Koffie koffie) {
        this.gerecht = gerecht;
        this.koffie = koffie;
    }

    public Gerecht getGerecht() {
        return gerecht;
    }

    public void setGerecht(Gerecht gerecht) {
        this.gerecht = gerecht;
    }

    public Koffie getKoffie() {
        return koffie;
    }

    public void setKoffie(Koffie koffie) {
        this.koffie = koffie;
    }
}
