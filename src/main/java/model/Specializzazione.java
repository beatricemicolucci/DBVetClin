package model;

import java.util.Objects;

public class Specializzazione {

    private final String field;

    public Specializzazione(final String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    @Override
    public String toString() {
        return "Specializzazione{" +
                "field='" + field + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specializzazione that = (Specializzazione) o;
        return Objects.equals(getField(), that.getField());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getField());
    }
}
