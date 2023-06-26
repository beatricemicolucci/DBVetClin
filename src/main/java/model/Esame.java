package model;

import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

public class Esame {

    private final int codExam;
    private final int idInvoice;
    private final String type;
    private final Date day;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int codVet;
    private final int codMedicalRecord;

    public Esame(final int codExam, final int idInvoice, final String type, final Date day, final LocalTime startTime, final LocalTime endTime, final int codVet, int codMedicalRecord) {
        this.codExam = codExam;
        this.idInvoice = idInvoice;
        this.type = type;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.codVet = codVet;
        this.codMedicalRecord = codMedicalRecord;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public int getCodMedicalRecord() {
        return codMedicalRecord;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public int getCodVet() {
        return codVet;
    }

    public Date getDay() {
        return day;
    }

    public int getCodExam() {
        return codExam;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Esame{" +
                "codExam=" + codExam +
                ", day=" + day +
                ", type='" + type + '\'' +
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
        Esame esame = (Esame) o;
        return getCodExam() == esame.getCodExam() && getIdInvoice() == esame.getIdInvoice() && getCodMedicalRecord() == esame.getCodMedicalRecord() && getCodVet() == esame.getCodVet() && Objects.equals(getDay(), esame.getDay()) && Objects.equals(getType(), esame.getType()) && Objects.equals(getStartTime(), esame.getStartTime()) && Objects.equals(getEndTime(), esame.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodExam(), getDay(), getType(), getStartTime(), getIdInvoice(), getEndTime(), getCodMedicalRecord(), getCodVet());
    }
}
