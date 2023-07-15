package model;

import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class Malattia {

    private final String description;
    private final Optional<Integer> idVet;
    private final Optional<Date> checkUpDay;
    private final Optional<LocalTime> checkUpTime;
    private final Optional<Integer> codExam;


    public Malattia(final String description, final Optional<Integer> idVet, final Optional<Date> checkUpDay, final Optional<LocalTime> checkUpTime, final Optional<Integer> codExam) {
        this.description = description;
        this.idVet = Objects.requireNonNull(idVet);
        this.checkUpDay = Objects.requireNonNull(checkUpDay);
        this.checkUpTime = Objects.requireNonNull(checkUpTime);
        this.codExam = Objects.requireNonNull(codExam);
    }

    public Malattia(final String description, final int idVet, final Date checkUpDay, final LocalTime checkUpTime) {
        this(description, Optional.ofNullable(idVet), Optional.ofNullable(checkUpDay), Optional.ofNullable(checkUpTime), Optional.empty());
    }

    public Malattia(final String description, final int idExam) {
        this(description, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(idExam));
    }

    public String getDescription() {
        return description;
    }

    public Optional<Integer> getIdVet() {
        return idVet;
    }

    public Optional<Date> getCheckUpDay() {
        return checkUpDay;
    }

    public Optional<LocalTime> getCheckUpTime() {
        return checkUpTime;
    }

    public Optional<Integer> getCodExam() {
        return codExam;
    }

    @Override
    public String toString() {
        return "Malattia{" +
                "description='" + description + '\'' +
                ", idVet=" + idVet +
                ", checkUpDay=" + checkUpDay +
                ", checkUpTime=" + checkUpTime +
                ", codExam=" + codExam +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Malattia malattia = (Malattia) o;
        return getIdVet() == malattia.getIdVet() && Objects.equals(getDescription(), malattia.getDescription()) && Objects.equals(getCheckUpDay(), malattia.getCheckUpDay()) && Objects.equals(getCheckUpTime(), malattia.getCheckUpTime()) && Objects.equals(getCodExam(), malattia.getCodExam());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getIdVet(), getCheckUpDay(), getCheckUpTime(), getCodExam());
    }
}
