package net.entcraft.broadcast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.entcraft.utils.Config;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Rules implements CommandExecutor, Listener {
	
	private Main plugin;

    public Rules(Main instance) {
        plugin = instance;
        File txtFile = new File(plugin.getDataFolder().toString() + File.separator + "rules.txt");
        if(!txtFile.exists()) {
            txtFile.getParentFile().mkdirs();
            Config.copy(plugin.getResource("rules.txt"), txtFile);
        }
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
        if(cmd.getName().equalsIgnoreCase("rules")) {
            try {
                List<String> l = plugin.processText(plugin.readTextFile(plugin.getDataFolder().toString() + File.separator + "rules.txt"));
                for(int i = 0; i < l.size(); i++) {
                    sender.sendMessage((String)l.get(i));
                }
            } catch(IOException e) {
                sender.sendMessage(ChatColor.RED + "Error: An IOException occured");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public void playerJoinForFirstTime(PlayerJoinEvent event) {
        if(!event.getPlayer().hasPlayedBefore()) {
            try {
                List<String> l = plugin.processText(plugin.readTextFile(plugin.getDataFolder().toString() + File.separator + "rules.txt"));
                for(int i = 0; i < l.size(); i++) {
                    event.getPlayer().sendMessage((String)l.get(i));
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}
