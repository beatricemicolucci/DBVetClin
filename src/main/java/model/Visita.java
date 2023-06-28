package model;

import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

public class Visita {

    private final Date day;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String type;

    public Visita(final Date day, final LocalTime startTime, final LocalTime endTime, final String type) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
    }

    public Date getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Visita{" +
                "day=" + day +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visita visita = (Visita) o;
        return Objects.equals(getDay(), visita.getDay()) && Objects.equals(getStartTime(), visita.getStartTime()) && Objects.equals(getEndTime(), visita.getEndTime()) && Objects.equals(getType(), visita.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDay(), getStartTime(), getEndTime(), getType());
    }
}
