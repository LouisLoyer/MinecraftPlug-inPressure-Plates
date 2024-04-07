package fr.bluesty.goldenpressureplate.listeners;

import fr.bluesty.goldenpressureplate.GoldenPressurePlate;
import fr.bluesty.goldenpressureplate.util.StorageManager;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.scheduler.BukkitRunnable;

public class PressurePlateListener implements Listener {

    private GoldenPressurePlate plugin;
    private StorageManager storageManager;

    public PressurePlateListener(GoldenPressurePlate plugin, StorageManager storageManager) {
        this.plugin = plugin;
        this.storageManager = storageManager;
    }

    // @EventHandler pour gérer PlayerMoveEvent et propulser le joueur si nécessaire
    @EventHandler
    public void onPlayerStepOnPressurePlate(PlayerMoveEvent event) {
        Location toLocation = event.getTo();
        Location fromLocation = event.getFrom();

        // S'assurer que le joueur a réellement changé de bloc pour éviter de déclencher plusieurs fois l'événement pour le même bloc
        if (toLocation.getBlock().equals(fromLocation.getBlock())) {
            return;
        }

        if (storageManager.isActivatedPlate(toLocation.getBlock().getLocation()) & (event.getTo().getBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE || event.getTo().getBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE)) {
            Player player = event.getPlayer();
            Vector displacement = toLocation.toVector().subtract(fromLocation.toVector()).normalize();
            double jumpPower = 1.5; // Puissance de la propulsion vers le haut

            Vector velocity = displacement.clone().normalize().multiply(10.0).setY(jumpPower);
            player.setVelocity(velocity);

            Location loc = player.getLocation();
            player.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
            player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 30);

            // Créer un BukkitRunnable pour appliquer la vélocité additionnelle sans propulsion verticale
            new BukkitRunnable() {
                int count = 0; // Compteur pour le nombre de répétitions

                @Override
                public void run() {
                    if (count < 14) { // Répéter l'action 10 fois
                        Vector currentVelocity = player.getVelocity();
                        player.setVelocity(displacement.clone().multiply(2.0).setY(currentVelocity.getY()));
                        count++;
                    } else {
                        this.cancel(); // Arrêter après 14 répétitions
                    }
                }
            }.runTaskTimer(plugin, 3L, 2L); // Démarrer après 3 ticks, répéter toutes les 2 ticks
        }
    }
}