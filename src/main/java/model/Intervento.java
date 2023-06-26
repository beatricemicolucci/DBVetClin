package model;

import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

public class Intervento {

    private final int operatingRoom;
    private final String type;
    private final Date date;
    private final LocalTime startTime;
    private final int idInvoice;
    private final LocalTime endTime;
    private final int codMedicalRecord;
    private final int codVet;

    public Intervento(final int operatingRoom, final String type, final Date date, final LocalTime startTime, final int idInvoice, final LocalTime endTime, final int codMedicalRecord, final int codVet) {
        this.operatingRoom = operatingRoom;
        this.type = type;
        this.date = date;
        this.startTime = startTime;
        this.idInvoice = idInvoice;
        this.endTime = endTime;
        this.codMedicalRecord = codMedicalRecord;
        this.codVet = codVet;
    }

    public int getOperatingRoom() {
        return operatingRoom;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getCodMedicalRecord() {
        return codMedicalRecord;
    }

    public int getCodVet() {
        return codVet;
    }

    @Override
    public String toString() {
        return "Intervento{" +
                "operatingRoom=" + operatingRoom +
                ", type='" + type + '\'' +
                ", date=" + date +
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
        Intervento that = (Intervento) o;
        return getOperatingRoom() == that.getOperatingRoom() && getIdInvoice() == that.getIdInvoice() && getCodMedicalRecord() == that.getCodMedicalRecord() && getCodVet() == that.getCodVet() && Objects.equals(getType(), that.getType()) && Objects.equals(getDate(), that.getDate()) && Objects.equals(getStartTime(), that.getStartTime()) && Objects.equals(getEndTime(), that.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOperatingRoom(), getType(), getDate(), getStartTime(), getIdInvoice(), getEndTime(), getCodMedicalRecord(), getCodVet());
    }
}
