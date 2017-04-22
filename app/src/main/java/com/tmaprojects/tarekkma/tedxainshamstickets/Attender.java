package com.tmaprojects.tarekkma.tedxainshamstickets;

/**
 * Created by tarekkma on 4/22/17.
 */

public class Attender {
    private int id;
    private String name;
    private String type;
    private String phoneNumber;
    private String email;
    private String nat_id;
    private String favColor;
    private boolean attended;

    public Attender(int id, String name, String type, String phoneNumber, String email, String nat_id, String favColor,boolean attended) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nat_id = nat_id;
        this.favColor = favColor;
        this.attended = attended;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNat_id() {
        return nat_id;
    }

    public void setNat_id(String nat_id) {
        this.nat_id = nat_id;
    }

    public String getFavColor() {
        return favColor;
    }

    public void setFavColor(String favColor) {
        this.favColor = favColor;

    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }
}
