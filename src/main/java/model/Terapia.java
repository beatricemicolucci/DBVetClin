package model;

import java.util.Objects;

public class Terapia {

    private final int id;
    private final String description;
    private final int idVet;
    private final int idMedicalRecord;


    public Terapia(final int id, final String description, final int idVet, final int idMedicalRecord) {
        this.id = id;
        this.description = description;
        this.idVet = idVet;
        this.idMedicalRecord = idMedicalRecord;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getIdVet() {
        return idVet;
    }

    public int getIdMedicalRecord() {
        return idMedicalRecord;
    }

    @Override
    public String toString() {
        return "Terapia{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", idVet=" + idVet +
                ", idMedicalRecord=" + idMedicalRecord +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Terapia terapia = (Terapia) o;
        return getId() == terapia.getId() && getIdVet() == terapia.getIdVet() && getIdMedicalRecord() == terapia.getIdMedicalRecord() && Objects.equals(getDescription(), terapia.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getIdVet(), getIdMedicalRecord());
    }
}
