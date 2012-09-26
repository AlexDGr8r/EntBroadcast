package net.entcraft.broadcast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.entcraft.utils.Config;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MOTD implements CommandExecutor, Listener {
	
	private Main plugin;

    public MOTD(Main instance) {
        plugin = instance;
        File txtFile = new File(plugin.getDataFolder().toString() + File.separator + "motd.txt");
        if(!txtFile.exists()) {
            txtFile.getParentFile().mkdirs();
            Config.copy(plugin.getResource("motd.txt"), txtFile);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void playerJoin(PlayerJoinEvent event) {
        try {
            List<String> l = plugin.processText(plugin.readTextFile(plugin.getDataFolder().toString() + File.separator + "motd.txt"));
            for(int i = 0; i < l.size(); i++) {
                event.getPlayer().sendMessage((String)l.get(i));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
        if(cmd.getName().equalsIgnoreCase("motd")) {
            try {
                List<String> l = plugin.processText(plugin.readTextFile(plugin.getDataFolder().toString() + File.separator + "motd.txt"));
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

}
