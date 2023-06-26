package model;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class Tecnico {

    private final int idTec;
    private final String cf;
    private  final String name;
    private final String lastName;
    private final Date birthDate;
    private final String address;
    private final String telephone;
    private final Optional<String> email;

    public Tecnico(final int idTec, final String cf, final String name, final String lastName, final Date birthDate, final String address, String telephone, final Optional<String> email) {
        this.idTec = idTec;
        this.cf = cf;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
    }

    public int getIdTec() {
        return idTec;
    }

    public String getCf() {
        return cf;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public Optional<String> getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Tecnico{" +
                "idTec=" + idTec +
                ", cf='" + cf + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email=" + email +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tecnico tecnico = (Tecnico) o;
        return getIdTec() == tecnico.getIdTec() && Objects.equals(getCf(), tecnico.getCf()) && Objects.equals(getName(), tecnico.getName()) && Objects.equals(getLastName(), tecnico.getLastName()) && Objects.equals(getBirthDate(), tecnico.getBirthDate()) && Objects.equals(getAddress(), tecnico.getAddress()) && Objects.equals(getTelephone(), tecnico.getTelephone()) && Objects.equals(getEmail(), tecnico.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdTec(), getCf(), getName(), getLastName(), getBirthDate(), getAddress(), getTelephone(), getEmail());
    }
}
