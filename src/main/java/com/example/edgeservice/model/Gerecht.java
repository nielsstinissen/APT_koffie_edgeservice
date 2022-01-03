package com.example.edgeservice.model;

public class Gerecht {
    private String id;
    private String koffieDrankNaam;
    private String naam;
    private String afkomst;
    private double kcal;
    private boolean isGlutenvrij;
    private boolean isVegan;
    private boolean isVegetarisch;
    private int aantalPersonen;
    private String url;

    public Gerecht(String koffieDrankNaam, String naam, String afkomst, double kcal, boolean isGlutenvrij, boolean isVegan, boolean isVegetarisch, int aantalPersonen, String url) {
        this.naam = naam;
        this.koffieDrankNaam = koffieDrankNaam;
        this.afkomst = afkomst;
        this.kcal = kcal;
        this.isGlutenvrij = isGlutenvrij;
        this.isVegan = isVegan;
        this.isVegetarisch = isVegetarisch;
        this.aantalPersonen = aantalPersonen;
        this.url = url;
    }

    public Gerecht() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getAfkomst() {
        return afkomst;
    }

    public void setAfkomst(String afkomst) {
        this.afkomst = afkomst;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public boolean isGlutenvrij() {
        return isGlutenvrij;
    }

    public void setGlutenvrij(boolean isGlutenvrij) {
        this.isGlutenvrij = isGlutenvrij;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    public boolean isVegetarisch() {
        return isVegetarisch;
    }

    public void setVegetarisch(boolean vegetarisch) {
        isVegetarisch = vegetarisch;
    }

    public int getAantalPersonen() {
        return aantalPersonen;
    }

    public void setAantalPersonen(int aantalPersonen) {
        this.aantalPersonen = aantalPersonen;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKoffieDrankNaam() {
        return koffieDrankNaam;
    }

    public void setKoffieDrankNaam(String koffieDrankNaam) {
        this.koffieDrankNaam = koffieDrankNaam;
    }
}
