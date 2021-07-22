package it.ancientrealms.townyconquered.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.palmergames.bukkit.towny.TownyEconomyHandler;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.event.NationRemoveTownEvent;
import com.palmergames.bukkit.towny.event.PreNewDayEvent;
import com.palmergames.bukkit.towny.event.nation.NationPreTownLeaveEvent;
import com.palmergames.bukkit.towny.event.time.dailytaxes.PreTownPaysNationTaxEvent;
import com.palmergames.bukkit.towny.object.Town;

import it.ancientrealms.townyconquered.ConqueredTown;
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
        List<ConqueredTown> remove = new ArrayList<>();

        for (ConqueredTown t : this.plugin.getConqueredManager().getListTowns())
        {
            t.incrementCount();

            final int current = t.getCount();
            final int end = Integer.valueOf(t.getDays());

            if (current > end)
            {
                remove.add(t);
            }
            else
            {
                this.plugin.getConqueredManager().save(t);
            }
        }

        for (ConqueredTown t : remove)
        {
            this.plugin.getConqueredManager().removeTown(this.towny.getTown(t.getTownUUID()));
        }
    }

    @EventHandler
    public void preTownPaysNationTax(PreTownPaysNationTaxEvent event)
    {
        final Town town = event.getTown();
        final Optional<ConqueredTown> conqueredtown = this.plugin.getConqueredManager().getTown(town);

        if (conqueredtown.isPresent())
        {
            final ConqueredTown ctown = conqueredtown.get();
            final int tax = Integer.valueOf(ctown.getTax());
            final String fmt = this.plugin.getMessagesManager().translate("tax_payment_to", event.getNation().getName());

            double paid = tax;

            switch (ctown.getTaxType())
            {
            case PERCENTAGE:
                paid = tax * TownySettings.getTownUpkeepCost(town) / 100;
                town.getAccount().payTo(tax * TownySettings.getTownUpkeepCost(town) / 100, event.getNation(), fmt);
                break;
            case FIXED:
                town.getAccount().payTo(tax, event.getNation(), fmt);
                break;
            }

            TownyMessaging.sendPrefixedTownMessage(town,
                    this.plugin.getMessagesManager().translate("payed_tax", TownyEconomyHandler.getFormattedBalance(paid)));
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
            event.setCancelMessage(this.plugin.getMessagesManager().translate("town_cannot_leave_nation_yet", event.getNationName()));
        }
    }
}
