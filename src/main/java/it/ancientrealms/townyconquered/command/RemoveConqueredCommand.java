package it.ancientrealms.townyconquered.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;

import it.ancientrealms.townyconquered.TownyConquered;

public final class RemoveConqueredCommand implements TabExecutor
{
    private final TownyConquered plugin;
    private final TownyUniverse towny = TownyUniverse.getInstance();

    public RemoveConqueredCommand(TownyConquered plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args)
    {
        List<String> s = new ArrayList<>();

        if (args.length == 1)
        {
            StringUtil.copyPartialMatches(args[0],
                    this.plugin.getConqueredManager().getListTowns().stream().map(t -> this.towny.getTown(t.getTownUUID()).getName()).toList(), s);
        }

        return s;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length != 1)
        {
            TownyMessaging.sendErrorMsg(sender, this.plugin.getCommand("removeconquered").getUsage());
            return true;
        }

        final String tname = args[0];
        final Town town = this.towny.getTown(tname);

        if (town == null)
        {
            TownyMessaging.sendErrorMsg(sender, this.plugin.getMessagesManager().translate("town_not_found", tname));
            return true;
        }

        if (!this.plugin.getConqueredManager().getTown(town).isPresent())
        {
            TownyMessaging.sendErrorMsg(sender, this.plugin.getMessagesManager().translate("town_not_conquered", tname));
            return true;
        }

        this.plugin.getConqueredManager().removeTown(town);

        return true;
    }
}
