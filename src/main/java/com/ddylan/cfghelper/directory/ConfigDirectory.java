package com.ddylan.cfghelper.directory;

import com.ddylan.cfghelper.ConfigHelper;
import com.ddylan.cfghelper.file.ConfigFile;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.File;
import java.util.*;

@Getter
public class ConfigDirectory {

    private final String directoryName, absPath;
    private final File directory;
    private final String dataExtension;
    private final Map<String, ConfigFile> dataFileCache;
    @Getter(AccessLevel.NONE) private final Set<ConfigFile> dataFileSet;

    public ConfigDirectory(File directory) {
        this.directoryName = directory.getName();
        this.absPath = directory.getAbsolutePath();
        this.directory = directory;
        this.dataExtension = ".yml"; // This is mainly here to filter file types. I might add support for other file types in the future.
        this.dataFileCache = new LinkedHashMap<>();
        this.dataFileSet = new LinkedHashSet<>();

        if (!exists()) {
            if (create()) {
                ConfigHelper.getInstance().log("Created directory " + directoryName + "!");
            } else {
                ConfigHelper.getInstance().err("Could not create directory " + directoryName + "!");
            }
        } else {
            if (!directory.isDirectory()) {
                throw new IllegalArgumentException("The file " + directoryName + " is not a directory!");
            }
            if (containsData()) {
                int found = 0;
                for (File file : getData()) {
                    if (file.getName().contains(dataExtension)) {
                        ConfigFile configFile = new ConfigFile(file.getName(), this);
                        ConfigHelper.getInstance().log("Found data file " + file.getName() + " in directory " + directoryName + "!");
                        dataFileCache.put(file.getName().replace(dataExtension, ""), configFile);
                        dataFileSet.add(configFile);
                        found++;
                    } else {
                        ConfigHelper.getInstance().log("Found non-configuration file " + file.getName() + " in directory " + directoryName + "!");
                    }
                }
                ConfigHelper.getInstance().log("Found " + found + " data files in directory " + directoryName + "!");
            } else {
                ConfigHelper.getInstance().log("No data files found in directory " + directoryName + "!");
            }
        }
    }

    public ConfigFile getDataFile(String fileName) {
        return dataFileCache.getOrDefault(fileName, new ConfigFile(fileName + dataExtension, this));
    }

    private List<File> getData() {
        return Arrays.asList(Objects.requireNonNull(directory.listFiles()));
    }

    private boolean containsData() {
        return directory.listFiles() != null && Objects.requireNonNull(directory.listFiles()).length > 0;
    }

    public ConfigDirectory(String directoryName, String absPath) {
        this(new File(absPath + File.separator + directoryName + File.separator));
    }

    public boolean create() {
        boolean created = directory.mkdirs();
        if (!directory.exists()) {
            created = directory.mkdirs();
        }
        return created;
    }

    public boolean exists() {
        return directory.exists();
    }

}
