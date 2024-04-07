package fr.bluesty.goldenpressureplate.commands;

import fr.bluesty.goldenpressureplate.GoldenPressurePlate;
import fr.bluesty.goldenpressureplate.util.StorageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.Material;

public class PropCreateCommand implements CommandExecutor {

    private GoldenPressurePlate plugin;
    private StorageManager storageManager;

    public PropCreateCommand(GoldenPressurePlate plugin, StorageManager storageManager) {
        this.plugin = plugin;
        this.storageManager = storageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.isOp()) {
            player.sendMessage("You do not have permission to execute this command.");
            return true;
        }

        // Obtenir le bloc sur lequel le joueur se tient
        Location blockUnderPlayer = player.getLocation().subtract(0, 0, 0).getBlock().getLocation();


        // Vérifier si le bloc est une plaque de pression
        if (blockUnderPlayer.getBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE || blockUnderPlayer.getBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
            // Ajouter l'emplacement à la liste des plaques activées
            storageManager.addActivatedPlate(blockUnderPlayer);
            player.sendMessage("Plaque de pression activée comme propulseur mon choux a la crème.");
        } else {
            player.sendMessage("Tu dois être sur une plaque de pression pour activer cette commande CONNARD.");
        }

        Material blockType = blockUnderPlayer.getBlock().getType();
        player.sendMessage("Bloc détecté: " + blockType.toString());

        return true;
    }
}
