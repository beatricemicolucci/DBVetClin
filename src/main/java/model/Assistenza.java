package model;

import java.util.Objects;

public class Assistenza {

    private final int codTec;
    private final int codVet;

    public Assistenza(int codTec, int codVet) {
        this.codTec = codTec;
        this.codVet = codVet;
    }

    public int getCodTec() {
        return codTec;
    }

    public int getCodVet() {
        return codVet;
    }

    @Override
    public String toString() {
        return "Assistenza{" +
                "codTec=" + codTec +
                ", codVet=" + codVet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assistenza that = (Assistenza) o;
        return getCodTec() == that.getCodTec() && getCodVet() == that.getCodVet();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodTec(), getCodVet());
    }
}
