package com.bitbucket.fcdev.townyconquered.manager;

public enum TaxType
{
    PERCENTAGE("percentuale"), FIXED("fisso");

    public final String label;

    private TaxType(String label)
    {
        this.label = label;
    }

    public static TaxType fromLabel(String label)
    {
        for (TaxType t : values())
        {
            if (t.label.equals(label))
            {
                return t;
            }
        }

        return null;
    }
}
