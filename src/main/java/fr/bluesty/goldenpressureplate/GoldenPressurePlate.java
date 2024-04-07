package fr.bluesty.goldenpressureplate;

import org.bukkit.plugin.java.JavaPlugin;
import fr.bluesty.goldenpressureplate.util.StorageManager;
import fr.bluesty.goldenpressureplate.commands.PropCreateCommand;
import fr.bluesty.goldenpressureplate.listeners.PressurePlateListener;

public class GoldenPressurePlate extends JavaPlugin {

    private StorageManager storageManager;

    @Override
    public void onEnable() {
        // Initialisation de la configuration
        saveDefaultConfig();

        // Initialisation et chargement du gestionnaire de stockage
        storageManager = new StorageManager(this);

        storageManager.reloadConfig();
        storageManager.loadActivatedPlates();

        // Enregistrement des commandes et des écouteurs
        if (this.getCommand("propcreate") != null) {
            this.getCommand("propcreate").setExecutor(new PropCreateCommand(this, storageManager));
        } else {
            getLogger().warning("La commande 'propcreate' n'a pas été trouvée. Vérifiez votre fichier plugin.yml.");
        }
        getServer().getPluginManager().registerEvents(new PressurePlateListener(this, storageManager), this);
    }

    @Override
    public void onDisable() {
        // Sauvegarde des plaques activées
        storageManager.saveActivatedPlates();
    }
}