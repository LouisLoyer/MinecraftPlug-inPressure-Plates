package fr.bluesty.goldenpressureplate.util;

import fr.bluesty.goldenpressureplate.GoldenPressurePlate;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;
import org.bukkit.World;
import org.bukkit.Bukkit;


public class StorageManager {

    private GoldenPressurePlate plugin;
    private Set<Location> activatedPlates;
    private File configFile;
    private FileConfiguration config;
    public StorageManager(GoldenPressurePlate plugin) {
        this.plugin = plugin;

        this.activatedPlates = new HashSet<>();
    }

    public void addActivatedPlate(Location location) {
        Location blockLocation = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        activatedPlates.add(blockLocation);
        saveActivatedPlates();
    }

    public boolean isActivatedPlate(Location location) {
        // Normaliser la localisation pour s'assurer que les coordonnées sont celles d'un bloc
        Location blockLocation = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());

        // Vérifier si l'ensemble des plaques activées contient la localisation normalisée
        return activatedPlates.contains(blockLocation);
    }

    public File getPluginDataFolder() {
        return plugin.getDataFolder();
    }

    public void saveActivatedPlates() {
        try {
            List<String> locations = activatedPlates.stream()
                    .map(loc -> loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ())
                    .collect(Collectors.toList());

            getConfig().set("activatedPlates", locations);
            getConfig().save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(getPluginDataFolder(), "activatedPlates.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    public void loadActivatedPlates() {
        getConfig().getStringList("activatedPlates").forEach(locString -> {
            String[] parts = locString.split(",");
            if (parts.length == 4) {
                World world = Bukkit.getWorld(parts[0]);
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int z = Integer.parseInt(parts[3]);
                Location loc = new Location(world, x, y, z);
                activatedPlates.add(loc);
            }
        });
    }
}
