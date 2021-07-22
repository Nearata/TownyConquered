package it.ancientrealms.townyconquered.listener;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.event.NationRemoveTownEvent;
import com.palmergames.bukkit.towny.event.PreNewDayEvent;
import com.palmergames.bukkit.towny.event.nation.NationPreTownLeaveEvent;
import com.palmergames.bukkit.towny.event.time.dailytaxes.PreTownPaysNationTaxEvent;
import com.palmergames.bukkit.towny.object.Town;

import it.ancientrealms.townyconquered.ITown;
import it.ancientrealms.townyconquered.TownyConquered;

public final class ConquerorListener implements Listener
{
    private final TownyConquered plugin;
    private final TownyUniverse towny = TownyUniverse.getInstance();

    public ConquerorListener(TownyConquered plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void preNewDay(PreNewDayEvent event)
    {
        List<ITown> remove = new ArrayList<>();

        for (ITown t : this.plugin.getConqueredManager().getListTowns())
        {
            final Instant ends = Timestamp.valueOf(t.getEnds()).toInstant();

            if (Instant.now().isAfter(ends))
            {
                remove.add(t);
            }
        }

        for (ITown t : remove)
        {
            this.plugin.getConqueredManager().removeTown(this.towny.getTown(t.getTownUUID()));
        }
    }

    @EventHandler
    public void preTownPaysNationTax(PreTownPaysNationTaxEvent event)
    {
        final Town town = event.getTown();
        final Optional<ITown> itown = this.plugin.getConqueredManager().getTown(town);

        if (itown.isPresent())
        {
            final ITown ltown = itown.get();
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
        final Town town = event.getTown();

        if (this.plugin.getConqueredManager().getTown(town).isPresent())
        {
            this.plugin.getConqueredManager().removeTown(town);
        }
    }

    @EventHandler
    public void nationPreTownLeave(NationPreTownLeaveEvent event)
    {
        final Town town = event.getTown();

        if (this.plugin.getConqueredManager().getTown(town).isPresent())
        {
            event.setCancelled(true);
            event.setCancelMessage("Your town can't leave the nation.");
        }
    }
}
