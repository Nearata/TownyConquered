package com.bitbucket.fcdev.townyconquered;

import org.bukkit.plugin.java.JavaPlugin;

public final class TownyConquered extends JavaPlugin
{
    private static TownyConquered INSTANCE;

    @Override
    public void onEnable()
    {
        INSTANCE = this;
    }

    @Override
    public void onDisable()
    {
    }

    public static TownyConquered getInstance()
    {
        return INSTANCE;
    }
}
