package it.ancientrealms.townyconquered;

import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import it.ancientrealms.townyconquered.command.RemoveConqueredCommand;
import it.ancientrealms.townyconquered.command.SetConqueredCommand;
import it.ancientrealms.townyconquered.listener.ConquerorListener;
import it.ancientrealms.townyconquered.manager.ConqueredManager;
import it.ancientrealms.townyconquered.manager.ITown;
import it.ancientrealms.townyconquered.manager.TaxType;

public final class TownyConquered extends JavaPlugin
{
    private static TownyConquered INSTANCE;
    private ConqueredManager conqueredManager;

    @Override
    public void onEnable()
    {
        INSTANCE = this;

        this.conqueredManager = new ConqueredManager(this);

        this.getCommand("setconquered").setExecutor(new SetConqueredCommand(this));
        this.getCommand("setconquered").setTabCompleter(new SetConqueredCommand(this));
        this.getCommand("removeconquered").setExecutor(new RemoveConqueredCommand(this));
        this.getCommand("removeconquered").setTabCompleter(new RemoveConqueredCommand(this));

        this.getServer().getPluginManager().registerEvents(new ConquerorListener(this), this);

        this.saveDefaultConfig();

        for (String tuuid : this.getConfig().getConfigurationSection("towns").getKeys(false))
        {
            final String nuuid = this.getConfig().getString(String.format("towns.%s.nation_uuid", tuuid));
            final String ends = this.getConfig().getString(String.format("towns.%s.ends", tuuid));
            final String tax = this.getConfig().getString(String.format("towns.%s.tax", tuuid));
            final String taxType = this.getConfig().getString(String.format("towns.%s.tax_type", tuuid));

            this.conqueredManager.getListTowns().add(new ITown(UUID.fromString(tuuid), UUID.fromString(nuuid), ends, tax, TaxType.fromLabel(taxType)));
        }
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
}
