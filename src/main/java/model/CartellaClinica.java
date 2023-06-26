package model;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class CartellaClinica {

    private final int codMedicalRec;
    private final int codAnimal;
    private final Date creationDate;

    public CartellaClinica(final int codMedicalRec, final int codAnimal, final Date creationDate) {
        this.codMedicalRec = codMedicalRec;
        this.codAnimal = codAnimal;
        this.creationDate = creationDate;
    }

    public int getCodMedicalRec() {
        return codMedicalRec;
    }

    public int getCodAnimal() {
        return codAnimal;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "CartellaClinica{" +
                "codMedicalRec=" + codMedicalRec +
                ", codAnimal=" + codAnimal +
                ", creationDate=" + creationDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartellaClinica that = (CartellaClinica) o;
        return getCodMedicalRec() == that.getCodMedicalRec() && getCodAnimal() == that.getCodAnimal() && Objects.equals(getCreationDate(), that.getCreationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodMedicalRec(), getCodAnimal(), getCreationDate());
    }
}
