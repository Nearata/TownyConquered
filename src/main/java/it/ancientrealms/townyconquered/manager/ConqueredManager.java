package it.ancientrealms.townyconquered.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.AlreadyRegisteredException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

import it.ancientrealms.townyconquered.TownyConquered;

public final class ConqueredManager
{
    private final TownyConquered plugin;
    private final TownyUniverse towny = TownyUniverse.getInstance();
    private List<ITown> towns = new ArrayList<>();

    public ConqueredManager(TownyConquered plugin)
    {
        this.plugin = plugin;
    }

    public void addConqueror(Town town, Nation nation, String days, String tax, TaxType taxType)
    {
        final UUID townUUID = town.getUUID();

        this.plugin.getConfig().set(String.format("towns.%s.nation_uuid", townUUID), nation.getUUID().toString());
        this.plugin.getConfig().set(String.format("towns.%s.tax", townUUID), tax);
        this.plugin.getConfig().set(String.format("towns.%s.tax_type", townUUID), taxType.label);
        this.plugin.saveConfig();
        this.plugin.reloadConfig();

        town.setConquered(true);
        town.setConqueredDays(Integer.valueOf(days));

        Nation oldNation = null;
        if (town.hasNation())
        {
            try
            {
                oldNation = town.getNation();
                town.removeNation();
            }
            catch (NotRegisteredException e)
            {
            }
        }

        try
        {
            town.setNation(nation);
        }
        catch (AlreadyRegisteredException e1)
        {
        }

        town.save();
        nation.save();

        if (oldNation != null)
        {
            oldNation.save();

            if (oldNation.getTowns().isEmpty())
            {
                this.towny.getDataSource().deleteNation(oldNation);
            }
        }

        this.towns.add(new ITown(townUUID, nation.getUUID(), tax, taxType));
    }

    public List<ITown> getTowns()
    {
        return this.towns;
    }
}
