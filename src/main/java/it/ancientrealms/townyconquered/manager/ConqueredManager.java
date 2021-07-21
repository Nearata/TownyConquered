package it.ancientrealms.townyconquered.manager;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.palmergames.bukkit.towny.TownyMessaging;
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

        final String ends = Timestamp.from(Instant.now().plus(Integer.valueOf(days), ChronoUnit.DAYS)).toString();

        this.plugin.getConfig().set(String.format("towns.%s.nation_uuid", townUUID), nation.getUUID().toString());
        this.plugin.getConfig().set(String.format("towns.%s.ends", townUUID), ends);
        this.plugin.getConfig().set(String.format("towns.%s.tax", townUUID), tax);
        this.plugin.getConfig().set(String.format("towns.%s.tax_type", townUUID), taxType.label);
        this.plugin.saveConfig();
        this.plugin.reloadConfig();

        this.towns.add(new ITown(townUUID, nation.getUUID(), ends, tax, taxType));

        TownyMessaging
                .sendGlobalMessage(String.format("The town %s is now under the control of the nation %s for %s days.", town.getName(), nation.getName(), days));
    }

    public void removeTown(Town town)
    {
        this.towns.removeIf(t -> t.getTownUUID() == town.getUUID());
        this.plugin.getConfig().set(String.format("towns.%s", town.getUUID()), null);
        this.plugin.saveConfig();
        this.plugin.reloadConfig();
    }

    public Optional<ITown> getTown(Town town)
    {
        return this.towns.stream().filter(t -> t.getTownUUID() == town.getUUID()).findAny();
    }

    public List<ITown> getListTowns()
    {
        return this.towns;
    }
}
