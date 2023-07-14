package model;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/***
 * The corresponding database table is:
 *    create table PADRONE (
 *      CF char(16) not null,
 *      Nome varchar(20) not null,
 *      Cognome varchar(20) not null,
 *      DataNascita date not null,
 *      Indirizzo varchar(30) not null,
 *      Telefono char(10) not null,
 *      IndirizzoEmail varchar(20),
 *      constraint IDPADRONE_ID primary key (CF));
 *   )
 */

public class Padrone {

    private final String cf;
    private  final String name;
    private final String lastName;
    private final Date birthDate;
    private String address;
    private final String telephone;
    private final Optional<String> email;

    public Padrone(final String cf, final String name, final String lastName, final Date birthDate, final String address, final String telephone, final Optional<String> email) {
        this.cf = cf;
        this.name = Objects.requireNonNull(name);
        this.lastName = Objects.requireNonNull(lastName);
        this.birthDate = Objects.requireNonNull(birthDate);
        this.address = Objects.requireNonNull(address);
        this.telephone = telephone;
        this.email = Objects.requireNonNull(email);
    }

    public Padrone(final String cf, final String name, final String lastName, final Date birthDate, final String address, final String telephone) {
        this(cf, name, lastName, birthDate, address, telephone, Optional.empty());
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

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Padrone{" +
                "cf='" + cf + '\'' +
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
        Padrone padrone = (Padrone) o;
        return Objects.equals(getCf(), padrone.getCf()) && Objects.equals(getName(), padrone.getName()) && Objects.equals(getLastName(), padrone.getLastName()) && Objects.equals(getBirthDate(), padrone.getBirthDate()) && Objects.equals(getAddress(), padrone.getAddress()) && Objects.equals(getTelephone(), padrone.getTelephone()) && Objects.equals(getEmail(), padrone.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCf(), getName(), getLastName(), getBirthDate(), getAddress(), getTelephone(), getEmail());
    }
}
