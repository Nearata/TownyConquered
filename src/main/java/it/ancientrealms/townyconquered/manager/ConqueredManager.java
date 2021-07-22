package it.ancientrealms.townyconquered.manager;

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

import it.ancientrealms.townyconquered.ITown;
import it.ancientrealms.townyconquered.TaxType;
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
        catch (AlreadyRegisteredException e)
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

        this.plugin.getConfig().set(String.format("towns.%s.nation_uuid", townUUID), nation.getUUID().toString());
        this.plugin.getConfig().set(String.format("towns.%s.days", townUUID), days);
        this.plugin.getConfig().set(String.format("towns.%s.count", townUUID), 0);
        this.plugin.getConfig().set(String.format("towns.%s.tax", townUUID), tax);
        this.plugin.getConfig().set(String.format("towns.%s.tax_type", townUUID), taxType.getLabel());
        this.plugin.saveConfig();
        this.plugin.reloadConfig();

        this.towns.add(new ITown(townUUID, nation.getUUID(), days, 0, tax, taxType));

        TownyMessaging
                .sendGlobalMessage(this.plugin.getMessagesConfig().getString("town_conquered").formatted(town.getName(), nation.getName(), days));
    }

    public void removeTown(Town town)
    {
        this.towns.removeIf(t -> t.getTownUUID() == town.getUUID());

        this.plugin.getConfig().set(String.format("towns.%s", town.getUUID()), null);
        this.plugin.saveConfig();
        this.plugin.reloadConfig();

        try
        {
            TownyMessaging
                    .sendGlobalMessage(this.plugin.getMessagesConfig().getString("town_unconquered").formatted(town.getName(), town.getNation().getName()));
        }
        catch (NotRegisteredException e)
        {
        }

        town.removeNation();
        town.save();
    }

    public Optional<ITown> getTown(Town town)
    {
        return this.towns.stream().filter(t -> t.getTownUUID().equals(town.getUUID())).findAny();
    }

    public List<ITown> getListTowns()
    {
        return this.towns;
    }

    public void save(ITown town)
    {
        final UUID tuuid = town.getTownUUID();

        this.plugin.getConfig().set(String.format("towns.%s.nation_uuid", tuuid), town.getNationUUID().toString());
        this.plugin.getConfig().set(String.format("towns.%s.days", tuuid), town.getDays());
        this.plugin.getConfig().set(String.format("towns.%s.count", tuuid), town.getCount());
        this.plugin.getConfig().set(String.format("towns.%s.tax", tuuid), town.getTax());
        this.plugin.getConfig().set(String.format("towns.%s.tax_type", tuuid), town.getTaxType().getLabel());
        this.plugin.saveConfig();
        this.plugin.reloadConfig();
    }
}
