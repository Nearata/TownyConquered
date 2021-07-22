package it.ancientrealms.townyconquered.manager;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import it.ancientrealms.townyconquered.TownyConquered;

public final class MessagesManager
{
    private final TownyConquered plugin;
    private File file;
    private FileConfiguration config;
    private final String filename = "messages.yml";

    public MessagesManager(TownyConquered plugin)
    {
        this.plugin = plugin;
        this.file = new File(this.plugin.getDataFolder(), this.filename);

        if (!this.file.exists())
        {
            this.plugin.saveResource(this.filename, false);
        }

        this.config = new YamlConfiguration();
    }

    public FileConfiguration getConfig()
    {
        return this.config;
    }

    public void reload()
    {
        try
        {
            this.config.load(this.file);
        }
        catch (IOException | InvalidConfigurationException e)
        {
        }
    }

    public String translate(String key, String... args)
    {
        return this.config.getString(key).formatted(String.join(",", args));
    }
}
