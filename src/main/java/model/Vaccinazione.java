package model;

import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

public class Vaccinazione {

    private final int idVet;
    private final Date day;
    private final LocalTime startTime;
    private final int idInvoice;
    private final LocalTime endTime;
    private final String disease;
    private final int idMedicalRecord;

    public Vaccinazione(final int idVet, final Date day, final LocalTime startTime, final int idInvoice, final LocalTime endTime, final String disease, final int idMedicalRecord) {
        this.idVet = idVet;
        this.day = day;
        this.startTime = startTime;
        this.idInvoice = idInvoice;
        this.endTime = endTime;
        this.disease = disease;
        this.idMedicalRecord = idMedicalRecord;
    }


    public int getIdInvoice() {
        return idInvoice;
    }

    public String getDisease() {
        return disease;
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

    public int getIdMedicalRecord() {
        return idMedicalRecord;
    }

    public int getIdVet() {
        return idVet;
    }

    @Override
    public String toString() {
        return "Vaccinazione{" +
                "idInvoice=" + idInvoice +
                ", disease='" + disease + '\'' +
                ", day=" + day +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", idMedicalRecord=" + idMedicalRecord +
                ", idVet=" + idVet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vaccinazione that = (Vaccinazione) o;
        return getIdInvoice() == that.getIdInvoice() && getIdMedicalRecord() == that.getIdMedicalRecord() && getIdVet() == that.getIdVet() && Objects.equals(getDisease(), that.getDisease()) && Objects.equals(getDay(), that.getDay()) && Objects.equals(getStartTime(), that.getStartTime()) && Objects.equals(getEndTime(), that.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdInvoice(), getDisease(), getDay(), getStartTime(), getEndTime(), getIdMedicalRecord(), getIdVet());
    }
}
