package model;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class Veterinario {

    private final int id;
    private final String office;
    private final String cf;
    private  final String name;
    private final String lastName;
    private final Date birthDate;
    private final String address;
    private final String telephone;
    private final Optional<String> email;

    public Veterinario(final int id, final String office, final String cf, final String name, final String lastName, final Date birthDate, final String address, final String telephone, final Optional<String> email) {
        this.office = office;
        this.id = id;
        this.cf = cf;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
    }

    public String getOffice() {
        return office;
    }

    public int getId() {
        return id;
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
        return "Veterinario{" +
                "id=" + id +
                ", office='" + office + '\'' +
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
        Veterinario that = (Veterinario) o;
        return getId() == that.getId() && Objects.equals(getOffice(), that.getOffice()) && Objects.equals(getCf(), that.getCf()) && Objects.equals(getName(), that.getName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getBirthDate(), that.getBirthDate()) && Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getTelephone(), that.getTelephone()) && Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOffice(), getCf(), getName(), getLastName(), getBirthDate(), getAddress(), getTelephone(), getEmail());
    }
}
