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
        File txtFile = new File((new StringBuilder(String.valueOf(instance.getDataFolder().toString()))).append(File.separator).append("broadcast.txt").toString());
        if(!txtFile.exists())
        {
            try
            {
                txtFile.createNewFile();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                if(enabled) {
                    try {
                        List<String> sList = processText(readTextFile((new StringBuilder(String.valueOf(Main.instance.getDataFolder().toString()))).append(File.separator).append("broadcast.txt").toString()));
                        for(int i = 0; i < sList.size(); i++) {
                            String s = (String)sList.get(i);
                            if(s.startsWith("@c")) {
                                processCommand(s);
                            } else {
                                Main.instance.getServer().broadcastMessage((String)sList.get(i));
                            }
                        }
                    }
                    catch(IOException e){
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
            s = s.replaceAll("&0", (new StringBuilder()).append(ChatColor.BLACK).toString());
            s = s.replaceAll("&1", (new StringBuilder()).append(ChatColor.DARK_BLUE).toString());
            s = s.replaceAll("&2", (new StringBuilder()).append(ChatColor.DARK_GREEN).toString());
            s = s.replaceAll("&3", (new StringBuilder()).append(ChatColor.DARK_AQUA).toString());
            s = s.replaceAll("&4", (new StringBuilder()).append(ChatColor.DARK_RED).toString());
            s = s.replaceAll("&5", (new StringBuilder()).append(ChatColor.DARK_PURPLE).toString());
            s = s.replaceAll("&6", (new StringBuilder()).append(ChatColor.GOLD).toString());
            s = s.replaceAll("&7", (new StringBuilder()).append(ChatColor.GRAY).toString());
            s = s.replaceAll("&8", (new StringBuilder()).append(ChatColor.DARK_GRAY).toString());
            s = s.replaceAll("&9", (new StringBuilder()).append(ChatColor.BLUE).toString());
            s = s.replaceAll("&a", (new StringBuilder()).append(ChatColor.GREEN).toString());
            s = s.replaceAll("&b", (new StringBuilder()).append(ChatColor.AQUA).toString());
            s = s.replaceAll("&c", (new StringBuilder()).append(ChatColor.RED).toString());
            s = s.replaceAll("&d", (new StringBuilder()).append(ChatColor.LIGHT_PURPLE).toString());
            s = s.replaceAll("&e", (new StringBuilder()).append(ChatColor.YELLOW).toString());
            s = s.replaceAll("&f", (new StringBuilder()).append(ChatColor.WHITE).toString());
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
