package model;

import java.util.Date;
import java.util.Objects;

public class Animale {

    private final int microchip;
    private final String name;
    private final String race;
    private final Date birthDate;
    private final String gender;
    private final String cfOwner;

    public Animale(final int microchip, final String name, final String race, final Date birthDate, final String gender, final String cfOwner) {
        this.microchip = microchip;
        this.name = name;
        this.race = race;
        this.birthDate = birthDate;
        this.gender = gender;
        this.cfOwner = cfOwner;
    }

    public int getMicrochip() {
        return microchip;
    }

    public String getName() {
        return name;
    }

    public String getRace() {
        return race;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getCfOwner() {
        return cfOwner;
    }

    @Override
    public String toString() {
        return "Animale{" +
                "microchip=" + microchip +
                ", name='" + name + '\'' +
                ", race='" + race + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", cfOwner='" + cfOwner + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animale animale = (Animale) o;
        return getMicrochip() == animale.getMicrochip() && Objects.equals(getName(), animale.getName()) && Objects.equals(getRace(), animale.getRace()) && Objects.equals(getBirthDate(), animale.getBirthDate()) && Objects.equals(getGender(), animale.getGender()) && Objects.equals(getCfOwner(), animale.getCfOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMicrochip(), getName(), getRace(), getBirthDate(), getGender(), getCfOwner());
    }
}
