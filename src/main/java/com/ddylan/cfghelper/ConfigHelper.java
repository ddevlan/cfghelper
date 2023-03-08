package com.ddylan.cfghelper;

import com.ddylan.cfghelper.directory.ConfigDirectory;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigHelper {

    private final String LOG_PREFIX = " [ConfigHelper] ";

    @Getter private static ConfigHelper instance;
    @Getter(AccessLevel.PROTECTED) private final JavaPlugin owningPlugin;
    @Getter(AccessLevel.PROTECTED) private final File cfgHelperFile;
    @Getter private final ConfigDirectory defaultConfigDirectory;

    public ConfigHelper(JavaPlugin plugin) {
        instance = this;
        this.owningPlugin = plugin;
        File pluginFolder = plugin.getDataFolder();
        this.cfgHelperFile = new File(pluginFolder, "cfg-helper.yml");

        /*
          this.configHelper = new ConfigHelper(instance);   // This is an example implemntation of ConfigHelper
          and an example implementation of configfiles and configdirectories

          The files are automatically created, and you can start using them right away!

          ConfigFile configFile = new ConfigFile("config");
        */
        this.defaultConfigDirectory = new ConfigDirectory(plugin.getName(), pluginFolder.getAbsolutePath());
    }

    public void log(String message) {
        owningPlugin.getLogger().info(LOG_PREFIX + message);
    }

    public void err(String message) {
        owningPlugin.getLogger().severe( LOG_PREFIX + message);
    }

}
