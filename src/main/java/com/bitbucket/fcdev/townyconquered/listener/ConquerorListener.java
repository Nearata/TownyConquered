package com.bitbucket.fcdev.townyconquered.listener;

import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bitbucket.fcdev.townyconquered.TownyConquered;
import com.bitbucket.fcdev.townyconquered.manager.ITown;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.event.NationRemoveTownEvent;
import com.palmergames.bukkit.towny.event.time.dailytaxes.PreTownPaysNationTaxEvent;
import com.palmergames.bukkit.towny.object.Town;

public final class ConquerorListener implements Listener
{
    private final TownyConquered plugin;

    public ConquerorListener(TownyConquered plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void preTownPaysNationTax(PreTownPaysNationTaxEvent event)
    {
        final Town town = event.getTown();
        final Optional<ITown> local = this.plugin.getConqueredManager().getTowns().stream().filter(t -> t.getTownUUID() == town.getUUID()).findAny();

        if (town.isConquered() && local.isPresent())
        {
            final ITown ltown = local.get();
            final int tax = Integer.valueOf(ltown.getTax());

            switch (ltown.getTaxType())
            {
            case PERCENTAGE:
                event.setTax(tax * TownySettings.getTownUpkeepCost(town) / 100);
                break;
            case FIXED:
                event.setTax(tax);
                break;
            }
        }
    }
    
    @EventHandler
    public void nationRemoveTown(NationRemoveTownEvent event)
    {
        // TODO
    }
}
