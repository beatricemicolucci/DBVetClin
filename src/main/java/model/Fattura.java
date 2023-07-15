package model;

import java.util.Date;
import java.util.Objects;

public class Fattura {

    private final int idInvoice;
    private final Date date;
    private final Float amount;
    private final String services;
    private final String cfOwner;


    public Fattura(final int idInvoice, final Date date, final Float amount, final String services, final String cfOwner) {
        this.idInvoice = idInvoice;
        this.date = date;
        this.amount = amount;
        this.services = services;
        this.cfOwner = cfOwner;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public Date getDate() {
        return date;
    }

    public Float getAmount() {
        return amount;
    }

    public String getCfOwner() {
        return cfOwner;
    }

    public String getServices() {
        return services;
    }

    @Override
    public String toString() {
        return "Fattura{" +
                "idInvoice=" + idInvoice +
                ", date=" + date +
                ", amount=" + amount +
                ", services='" + services + '\'' +
                ", cfOwner='" + cfOwner + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fattura fattura = (Fattura) o;
        return getIdInvoice() == fattura.getIdInvoice() && Objects.equals(getDate(), fattura.getDate()) && Objects.equals(getAmount(), fattura.getAmount()) && Objects.equals(getServices(), fattura.getServices()) && Objects.equals(getCfOwner(), fattura.getCfOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdInvoice(), getDate(), getAmount(), getServices(), getCfOwner());
    }
}
