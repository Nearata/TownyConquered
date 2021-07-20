package com.bitbucket.fcdev.townyconquered.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bitbucket.fcdev.townyconquered.TownyConquered;
import com.bitbucket.fcdev.townyconquered.manager.TaxType;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

public final class SetConquerorCommand implements TabExecutor
{
    private final TownyUniverse towny = TownyUniverse.getInstance();
    private final TownyConquered plugin;

    public SetConquerorCommand(TownyConquered plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args)
    {
        List<String> s = new ArrayList<>();

        switch (args.length)
        {
        case 1:
            StringUtil.copyPartialMatches(args[0], this.towny.getTowns().stream().map(n -> n.getName()).toList(), s);
            break;
        case 2:
            StringUtil.copyPartialMatches(args[1], this.towny.getNations().stream().map(t -> t.getName()).toList(), s);
            break;
        case 5:
            StringUtil.copyPartialMatches(args[4], Arrays.stream(TaxType.values()).map(t -> t.label).collect(Collectors.toList()), s);
            break;
        }

        return s;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length != 5)
        {
            sender.sendMessage(this.plugin.getCommand("setconqueror").getUsage());
            return true;
        }

        final Town town = this.towny.getTown(args[0]);
        final Nation nation = this.towny.getNation(args[1]);
        final String days = args[2];
        final String tax = args[3];
        final TaxType taxType = TaxType.fromLabel(args[4]);

        if (taxType == null)
        {
            sender.sendMessage("Questo tipo di tassa non esiste!");
            return true;
        }

        if (nation == null)
        {
            sender.sendMessage("La nazione non esiste!");
            return true;
        }

        if (town == null)
        {
            sender.sendMessage("La town non esiste!");
            return true;
        }

        this.plugin.getConqueredManager().addConqueror(town, nation, days, tax, taxType);

        return true;
    }
}
