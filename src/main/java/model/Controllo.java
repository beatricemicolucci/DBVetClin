package model;

import java.util.Date;
import java.time.LocalTime;
import java.util.Objects;

public class Controllo {

    private final int codVet;
    private final Date day;
    private final LocalTime startTime;
    private final int idInvoice;
    private final LocalTime endTime;
    private final int codMedicalRecord;


    public Controllo(final int codVet, final Date day, final LocalTime startTime, final int idInvoice, final LocalTime endTime, final int codMedicalRecord) {
        this.day = day;
        this.startTime = startTime;
        this.idInvoice = idInvoice;
        this.endTime = endTime;
        this.codMedicalRecord = codMedicalRecord;
        this.codVet = codVet;
    }

    public Date getDay() {
        return day;
    }

    public int getCodVet() {
        return codVet;
    }

    public int getCodMedicalRecord() {
        return codMedicalRecord;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Controllo{" +
                "day=" + day +
                ", startTime=" + startTime +
                ", idInvoice=" + idInvoice +
                ", endTime=" + endTime +
                ", codMedicalRecord=" + codMedicalRecord +
                ", codVet=" + codVet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Controllo controllo = (Controllo) o;
        return getIdInvoice() == controllo.getIdInvoice() && getCodMedicalRecord() == controllo.getCodMedicalRecord() && getCodVet() == controllo.getCodVet() && Objects.equals(getDay(), controllo.getDay()) && Objects.equals(getStartTime(), controllo.getStartTime()) && Objects.equals(getEndTime(), controllo.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDay(), getStartTime(), getIdInvoice(), getEndTime(), getCodMedicalRecord(), getCodVet());
    }
}
