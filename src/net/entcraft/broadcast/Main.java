package net.entcraft.broadcast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import net.entcraft.utils.Config;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private static Main instance;
    private Config config;
    private Logger log;
    private long interval;
    private boolean enabled;
    
    public void onEnable() {
        instance = this;
        config = new Config(this);
        log = getLogger();
        loadData();
        MOTD motd = new MOTD(this);
        Rules rules = new Rules(this);
        getServer().getPluginManager().registerEvents(motd, this);
        getServer().getPluginManager().registerEvents(rules, this);
        getCommand("motd").setExecutor(motd);
        getCommand("rules").setExecutor(rules);
        File txtFile = new File(instance.getDataFolder().toString() + File.separator + "broadcast.txt");
        if(!txtFile.exists()) {
            try {
                txtFile.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                if(enabled) {
                    try {
                        List<String> sList = processText(readTextFile(Main.instance.getDataFolder().toString() + File.separator + "broadcast.txt"));
                        for(int i = 0; i < sList.size(); i++) {
                            String s = (String)sList.get(i);
                            if(s.startsWith("@c")) {
                                processCommand(s);
                            } else {
                                Main.instance.getServer().broadcastMessage((String)sList.get(i));
                            }
                        }
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 20L, 20L * interval);
    }
    
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        log.info("Disabled!");
    }
    
    public List<String> readTextFile(String fileName) throws IOException {
            java.nio.file.Path path = Paths.get(fileName, new String[0]);
            return Files.readAllLines(path, StandardCharsets.UTF_8);
    }
    
    public List<String> processText(List<String> sList) {
        for(int i = 0; i < sList.size(); i++) {
            String s = (String)sList.get(i);
            s = s.replaceAll("&0", ChatColor.BLACK + "");
            s = s.replaceAll("&1", ChatColor.DARK_BLUE + "");
            s = s.replaceAll("&2", ChatColor.DARK_GREEN + "");
            s = s.replaceAll("&3", ChatColor.DARK_AQUA + "");
            s = s.replaceAll("&4", ChatColor.DARK_RED + "");
            s = s.replaceAll("&5", ChatColor.DARK_PURPLE + "");
            s = s.replaceAll("&6", ChatColor.GOLD + "");
            s = s.replaceAll("&7", ChatColor.GRAY + "");
            s = s.replaceAll("&8", ChatColor.DARK_GRAY + "");
            s = s.replaceAll("&9", ChatColor.BLUE + "");
            s = s.replaceAll("&a", ChatColor.GREEN + "");
            s = s.replaceAll("&b", ChatColor.AQUA + "");
            s = s.replaceAll("&c", ChatColor.RED + "");
            s = s.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
            s = s.replaceAll("&e", ChatColor.YELLOW + "");
            s = s.replaceAll("&f", ChatColor.WHITE + "");
            sList.set(i, s);
        }
        return sList;
    }
    
    public void processCommand(String s) {
        String cmd = s.substring(3);
        getServer().dispatchCommand(getServer().getConsoleSender(), cmd);
    }

    private void loadData() {
        interval = config.get("interval", 300L);
        enabled = config.get("enabled", true);
        config.saveAllData();
    }

}
