package model;

import java.util.Objects;

public class Competenza {

    private final String field;
    private final int codVet;


    public Competenza(final String field, final int codVet) {
        this.field = field;
        this.codVet = codVet;
    }

    public String getField() {
        return field;
    }

    public int getCodVet() {
        return codVet;
    }

    @Override
    public String toString() {
        return "Competenza{" +
                "field='" + field + '\'' +
                ", codVet=" + codVet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Competenza that = (Competenza) o;
        return getCodVet() == that.getCodVet() && Objects.equals(getField(), that.getField());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getField(), getCodVet());
    }
}
