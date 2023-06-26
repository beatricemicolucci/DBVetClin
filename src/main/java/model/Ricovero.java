package model;

import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

public class Ricovero {

    private final Date startDate;
    private final Date endDate;
    private final int idHospitalization;
    private final Date operationDay;
    private final LocalTime operationTime;
    private final int idOperatingRoom;


    public Ricovero(final Date startDate, final Date endDate, final int idHospitalization, final Date operationDay, final LocalTime operationTime, final int idOperatingRoom) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.idHospitalization = idHospitalization;
        this.operationDay = operationDay;
        this.operationTime = operationTime;
        this.idOperatingRoom = idOperatingRoom;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getIdHospitalization() {
        return idHospitalization;
    }

    public Date getOperationDay() {
        return operationDay;
    }

    public LocalTime getOperationTime() {
        return operationTime;
    }

    public int getIdOperatingRoom() {
        return idOperatingRoom;
    }

    @Override
    public String toString() {
        return "Ricovero{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", idHospitalization=" + idHospitalization +
                ", operationDay=" + operationDay +
                ", operationTime=" + operationTime +
                ", idOperatingRoom=" + idOperatingRoom +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ricovero ricovero = (Ricovero) o;
        return getIdHospitalization() == ricovero.getIdHospitalization() && getIdOperatingRoom() == ricovero.getIdOperatingRoom() && Objects.equals(getStartDate(), ricovero.getStartDate()) && Objects.equals(getEndDate(), ricovero.getEndDate()) && Objects.equals(getOperationDay(), ricovero.getOperationDay()) && Objects.equals(getOperationTime(), ricovero.getOperationTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartDate(), getEndDate(), getIdHospitalization(), getOperationDay(), getOperationTime(), getIdOperatingRoom());
    }
}
