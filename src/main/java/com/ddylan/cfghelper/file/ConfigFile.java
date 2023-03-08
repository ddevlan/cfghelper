package com.ddylan.cfghelper.file;

import com.ddylan.cfghelper.ConfigHelper;
import com.ddylan.cfghelper.directory.ConfigDirectory;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConfigFile implements AutoCloseable {

    @Getter private final String fileName;
    private final File file;
    private YamlConfiguration config;
    @Getter private final Map<String, Object> values;
    private final Map<String, Long> lastModified;
    private final ConfigDirectory directory;

    public ConfigFile(String fileName) {
        this(fileName, ConfigHelper.getInstance().getDefaultConfigDirectory());
    }

    public ConfigFile(String fileName, ConfigDirectory directory) {
        this.fileName = directory.getDirectoryName() + File.separator + fileName;

        if (!directory.exists()) directory.create();
        this.directory = directory;

        this.file = new File(directory.getAbsPath() + File.separator + fileName + directory.getDataExtension());

        this.values = new TreeMap<>();
        this.lastModified = new HashMap<>();

        if (!exists()) {
            if (create()) {
                ConfigHelper.getInstance().log("Created file " + fileName + "!");
            } else {
                ConfigHelper.getInstance().err("Could not create file " + fileName + "!");
            }
        } else {
            if (!file.isFile()) {
                throw new IllegalArgumentException("The file " + fileName + " is not a file!");
            } else {
                if (load()) {
                    ConfigHelper.getInstance().log("Loaded file " + fileName + "!");
                } else {
                    ConfigHelper.getInstance().err("Could not load file " + fileName + "!");
                }
            }
        }
    }

    private boolean load() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
            for (String key : config.getKeys(true)) {
                values.put(key, config.get(key));
                lastModified.put(key, file.lastModified());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean exists() {
        return file.exists();
    }

    public boolean create() {
        try {
            return file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean save() {
        try {
            if (config == null) {
                config = YamlConfiguration.loadConfiguration(file);
            }
            for (String key : values.keySet()) {
                config.set(key, values.get(key));
            }

            config.save(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete() {
        try {
            return file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean reload() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isModified(String key) {
        return lastModified.get(key) != file.lastModified();
    }

    public boolean isModified() {
        for (String key : values.keySet()) {
            if (isModified(key)) return true;
        }
        return false;
    }

    public boolean isModified(String... keys) {
        for (String key : keys) {
            if (isModified(key)) return true;
        }
        return false;
    }

    public String getString(String key) {
        if (isModified(key)) {
            values.put(key, config.getString(key));
            lastModified.put(key, file.lastModified());
        }
        return (String) values.get(key);
    }

    public List<String> getStringList(String key) {
        if (isModified(key)) {
            values.put(key, config.getStringList(key));
            lastModified.put(key, file.lastModified());
        }
        return (List<String>) values.get(key);
    }

    public int getInt(String key) {
        if (isModified(key)) {
            values.put(key, config.getInt(key));
            lastModified.put(key, file.lastModified());
        }
        return (int) values.get(key);
    }

    public long getLong(String key) {
        if (isModified(key)) {
            values.put(key, config.getLong(key));
            lastModified.put(key, file.lastModified());
        }
        return (long) values.get(key);
    }

    public float getFloat(String key) {
        if (isModified(key)) {
            values.put(key, config.getFloat(key));
            lastModified.put(key, file.lastModified());
        }
        return (float) values.get(key);
    }

    public double getDouble(String key) {
        if (isModified(key)) {
            values.put(key, config.getDouble(key));
            lastModified.put(key, file.lastModified());
        }
        return (double) values.get(key);
    }

    public boolean getBoolean(String key) {
        if (isModified(key)) {
            values.put(key, config.getBoolean(key));
            lastModified.put(key, file.lastModified());
        }
        return (boolean) values.get(key);
    }

    public Object get(String key) {
        if (isModified(key)) {
            values.put(key, config.get(key));
            lastModified.put(key, file.lastModified());
        }
        return values.get(key);
    }

    public void set(String key, Object value) {
        config.set(key, value);
        values.put(key, value);
        lastModified.put(key, file.lastModified());
    }

    public void set(String key, Object value, boolean save) {
        set(key, value);
        if (save) save();
    }

    public ConfigurationSection getConfigurationSection(String key) {
        if (isModified(key)) {
            values.put(key, config.getConfigurationSection(key));
            lastModified.put(key, file.lastModified());
        }
        return (ConfigurationSection) values.get(key);
    }

    @Override
    public void close() {
        if (save()) {
            ConfigHelper.getInstance().log("Saved file " + fileName + "!");
        } else {
            ConfigHelper.getInstance().err("Could not save file " + fileName + "!");
        }
    }
}
