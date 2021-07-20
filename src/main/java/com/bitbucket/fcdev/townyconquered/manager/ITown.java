package com.bitbucket.fcdev.townyconquered.manager;

import java.util.UUID;

public final class ITown
{
    private final UUID townUUID;
    private final UUID nationUUID;
    private String tax;
    private TaxType taxType;

    public ITown(UUID townUUID, UUID nationUUID, String tax, TaxType taxType)
    {
        this.townUUID = townUUID;
        this.nationUUID = nationUUID;
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

    public String getTax()
    {
        return this.tax;
    }

    public TaxType getTaxType()
    {
        return this.taxType;
    }
}
