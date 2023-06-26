package model;

import java.util.Objects;

public class Ingrediente {

    private final String name;
    private final int idMedicine;


    public Ingrediente(final String name, final int idMedicine) {
        this.name = name;
        this.idMedicine = idMedicine;
    }

    public String getName() {
        return name;
    }

    public int getIdMedicine() {
        return idMedicine;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "name='" + name + '\'' +
                ", idMedicine=" + idMedicine +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingrediente that = (Ingrediente) o;
        return getIdMedicine() == that.getIdMedicine() && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getIdMedicine());
    }
}
