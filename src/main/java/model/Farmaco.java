package model;

import java.util.Objects;

public class Farmaco {

    private final int codMed;

    public Farmaco(final int codMed) {
        this.codMed = codMed;
    }

    public int getCodMed() {
        return codMed;
    }

    @Override
    public String toString() {
        return "Farmaco{" +
                "codMed=" + codMed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Farmaco farmaco = (Farmaco) o;
        return getCodMed() == farmaco.getCodMed();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodMed());
    }
}
