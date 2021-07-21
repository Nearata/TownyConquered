package it.ancientrealms.townyconquered.manager;

import java.util.UUID;

public final class ITown
{
    private final UUID townUUID;
    private final UUID nationUUID;
    private String ends;
    private String tax;
    private TaxType taxType;

    public ITown(UUID townUUID, UUID nationUUID, String ends, String tax, TaxType taxType)
    {
        this.townUUID = townUUID;
        this.nationUUID = nationUUID;
        this.ends = ends;
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

    public String getEnds()
    {
        return this.ends;
    }

    public String getTax()
    {
        return this.tax;
    }

    public TaxType getTaxType()
    {
        return this.taxType;
    }
}
