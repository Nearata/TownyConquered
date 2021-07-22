package it.ancientrealms.townyconquered.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.palmergames.bukkit.towny.TownyMessaging;

import it.ancientrealms.townyconquered.TownyConquered;

public final class ReloadCommand implements CommandExecutor
{
    private final TownyConquered plugin;

    public ReloadCommand(TownyConquered plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length > 0)
        {
            TownyMessaging.sendErrorMsg(sender, this.plugin.getCommand("reload").getUsage());
            return true;
        }

        this.plugin.getConqueredManager().reload();
        this.plugin.getMessagesManager().reload();

        TownyMessaging.sendMsg(sender, this.plugin.getMessagesManager().getConfig().getString("reload_success"));

        return true;
    }
}
