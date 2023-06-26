package model;

import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class Malattia {

    private final String description;
    private final int idVet;
    private final Optional<Date> checkUpDay;
    private final Optional<LocalTime> checkUpTime;
    private final Optional<Integer> codExam;


    public Malattia(final String description, final int idVet, final Optional<Date> checkUpDay, final Optional<LocalTime> checkUpTime, final Optional<Integer> codExam) {
        this.description = description;
        this.idVet = idVet;
        this.checkUpDay = checkUpDay;
        this.checkUpTime = checkUpTime;
        this.codExam = codExam;
    }

    public String getDescription() {
        return description;
    }

    public int getIdVet() {
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
