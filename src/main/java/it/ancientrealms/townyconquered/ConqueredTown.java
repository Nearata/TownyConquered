package it.ancientrealms.townyconquered;

import java.util.UUID;

public final class ConqueredTown
{
    private final UUID townUUID;
    private final UUID nationUUID;
    private final String days;
    private int count;
    private final String tax;
    private final TaxType taxType;

    public ConqueredTown(UUID townUUID, UUID nationUUID, String days, int count, String tax, TaxType taxType)
    {
        this.townUUID = townUUID;
        this.nationUUID = nationUUID;
        this.days = days;
        this.count = count;
        this.tax = tax;
        this.taxType = taxType;
    }

    public UUID getTownUUID()
    {
        return this.townUUID;
    }

    public UUID getNationUUID()
    {
        return this.nationUUID;
    }

    public String getDays()
    {
        return this.days;
    }

    public int getCount()
    {
        return this.count;
    }

    public String getTax()
    {
        return this.tax;
    }

    public TaxType getTaxType()
    {
        return this.taxType;
    }

    public void incrementCount()
    {
        this.count++;
    }
}
