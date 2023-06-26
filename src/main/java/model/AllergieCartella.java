package model;

import java.util.Objects;

public class AllergieCartella {

    private final int idMedRecord;
    private final int idAllergy;

    public AllergieCartella(final int idMedRecord, final int idAllergy) {
        this.idMedRecord = idMedRecord;
        this.idAllergy = idAllergy;
    }

    public int getIdMedRecord() {
        return idMedRecord;
    }

    public int getIdAllergy() {
        return idAllergy;
    }

    @Override
    public String toString() {
        return "AllergieCartella{" +
                "idMedRecord=" + idMedRecord +
                ", idAllergy=" + idAllergy +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllergieCartella that = (AllergieCartella) o;
        return getIdMedRecord() == that.getIdMedRecord() && getIdAllergy() == that.getIdAllergy();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdMedRecord(), getIdAllergy());
    }
}
