package it.ancientrealms.townyconquered;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import it.ancientrealms.townyconquered.command.RemoveConqueredCommand;
import it.ancientrealms.townyconquered.command.SetConqueredCommand;
import it.ancientrealms.townyconquered.listener.ConquerorListener;
import it.ancientrealms.townyconquered.manager.ConqueredManager;

public final class TownyConquered extends JavaPlugin
{
    private static TownyConquered INSTANCE;
    private ConqueredManager conqueredManager;
    private File messagesFile;
    private FileConfiguration messagesConfig;

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
            final String days = this.getConfig().getString(String.format("towns.%s.days", tuuid));
            final String count = this.getConfig().getString(String.format("towns.%s.count", tuuid));
            final String tax = this.getConfig().getString(String.format("towns.%s.tax", tuuid));
            final String taxType = this.getConfig().getString(String.format("towns.%s.tax_type", tuuid));

            this.conqueredManager.getListTowns()
                    .add(new ITown(UUID.fromString(tuuid), UUID.fromString(nuuid), days, Integer.valueOf(count), tax, TaxType.fromLabel(taxType)));
        }

        this.messagesFile = new File(this.getDataFolder(), "messages.yml");

        if (!this.messagesFile.exists())
        {
            this.saveResource("messages.yml", false);
        }

        this.messagesConfig = new YamlConfiguration();
        try
        {
            this.messagesConfig.load(this.messagesFile);
        }
        catch (IOException | InvalidConfigurationException e)
        {
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

    public FileConfiguration getMessagesConfig()
    {
        return this.messagesConfig;
    }
}
