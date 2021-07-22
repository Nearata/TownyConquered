package it.ancientrealms.townyconquered;

import org.bukkit.plugin.java.JavaPlugin;

import it.ancientrealms.townyconquered.command.ReloadCommand;
import it.ancientrealms.townyconquered.command.RemoveConqueredCommand;
import it.ancientrealms.townyconquered.command.SetConqueredCommand;
import it.ancientrealms.townyconquered.listener.ConqueredListener;
import it.ancientrealms.townyconquered.manager.ConqueredManager;
import it.ancientrealms.townyconquered.manager.MessagesManager;

public final class TownyConquered extends JavaPlugin
{
    private static TownyConquered INSTANCE;
    private ConqueredManager conqueredManager;
    private MessagesManager messagesManager;

    @Override
    public void onEnable()
    {
        INSTANCE = this;

        this.conqueredManager = new ConqueredManager(this);
        this.messagesManager = new MessagesManager(this);

        this.getCommand("setconquered").setExecutor(new SetConqueredCommand(this));
        this.getCommand("setconquered").setTabCompleter(new SetConqueredCommand(this));
        this.getCommand("removeconquered").setExecutor(new RemoveConqueredCommand(this));
        this.getCommand("removeconquered").setTabCompleter(new RemoveConqueredCommand(this));
        this.getCommand("reload").setExecutor(new ReloadCommand(this));

        this.getServer().getPluginManager().registerEvents(new ConqueredListener(this), this);

        this.saveDefaultConfig();

        this.conqueredManager.reload();
        this.messagesManager.reload();
    }

    @Override
    public void onDisable()
    {
    }

    public static TownyConquered getInstance()
    {
        return INSTANCE;
    }

    public ConqueredManager getConqueredManager()
    {
        return this.conqueredManager;
    }

    public MessagesManager getMessagesManager()
    {
        return this.messagesManager;
    }
}
