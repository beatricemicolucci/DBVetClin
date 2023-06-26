package model;

import java.util.Objects;

public class Composizione {

    private final int codMed;
    private final int codTherapy;

    public Composizione(final int codMed, final int codTherapy) {
        this.codMed = codMed;
        this.codTherapy = codTherapy;
    }

    public int getCodMed() {
        return codMed;
    }

    public int getCodTherapy() {
        return codTherapy;
    }

    @Override
    public String toString() {
        return "Composizione{" +
                "codMed=" + codMed +
                ", codTherapy=" + codTherapy +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Composizione that = (Composizione) o;
        return getCodMed() == that.getCodMed() && getCodTherapy() == that.getCodTherapy();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodMed(), getCodTherapy());
    }
}
