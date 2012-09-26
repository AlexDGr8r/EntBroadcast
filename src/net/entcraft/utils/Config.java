package net.entcraft.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Config {
	
	private File fileConfig;
	private YamlConfiguration config;
	private JavaPlugin plugin;
	private HashMap<String, Object> saveData = new HashMap<String, Object>();
	
	public Config(JavaPlugin instance) {
		plugin = instance;
		fileConfig = new File(plugin.getDataFolder(), "config.yml");
		if (!fileConfig.exists()) {
			fileConfig.getParentFile().mkdirs();
			try {
				fileConfig.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = new YamlConfiguration();
		try {
			config.load(fileConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Config(JavaPlugin instance, String fileName) {
		plugin = instance;
		fileConfig = new File(plugin.getDataFolder(), fileName);
		if (!fileConfig.exists()) {
			fileConfig.getParentFile().mkdirs();
			try {
				fileConfig.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = new YamlConfiguration();
		try {
			config.load(fileConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public YamlConfiguration getConfig() {
		return config;
	}
	
	public void set(String node, Object object) {
		config.set(node, object);
		try {
			config.save(fileConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void put(String node, Object o) {
		saveData.put(node, o);
	}
	
	public void saveAllData() {
		try {
			config.load(fileConfig);
			for (String s : saveData.keySet()) {
				config.set(s, saveData.get(s));
			}
			config.save(fileConfig);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public Object get(String node) {
		Object o = config.get(node);
		saveData.put(node, o);
		return o;
	}
	
	public boolean get(String node, boolean b) {
		boolean b1 = config.getBoolean(node, b);
		saveData.put(node, b1);
		return b1;
	}
	
	public List<Boolean> getBooleanList(String node) {
		List<Boolean> b = config.getBooleanList(node);
		saveData.put(node, b);
		return b;
	}
	
	public double get(String node, double d) {
		double d1 = config.getDouble(node, d);
		saveData.put(node, d1);
		return d1;
	}
	
	public List<Double> getDoubleList(String node) {
		List<Double> d = config.getDoubleList(node);
		saveData.put(node, d);
		return d;
	}
	
	public List<Float> getFloatList(String node) {
		List<Float> f = config.getFloatList(node);
		saveData.put(node, f);
		return f;
	}
	
	public int get(String node, int i) {
		int i1 = config.getInt(node, i);
		saveData.put(node, i1);
		return i1;
	}
	
	public List<Integer> getIntList(String node) {
		List<Integer> l = config.getIntegerList(node);
		saveData.put(node, l);
		return l;
	}
	
	public ItemStack get(String node, ItemStack i) {
		ItemStack i1 = config.getItemStack(node, i);
		saveData.put(node, i1);
		return i1;
	}
	
	public long get(String node, long l) {
		long l1 = config.getLong(node, l);
		saveData.put(node, l1);
		return l1;
	}
	
	public List<Long> getLongList(String node) {
		List<Long> l = config.getLongList(node);
		saveData.put(node, l);
		return l;
	}
	
	public OfflinePlayer get(String node, OfflinePlayer p) {
		OfflinePlayer p1 = config.getOfflinePlayer(node, p);
		saveData.put(node, p1);
		return p1;
	}
	
	public String get(String node, String s) {
		String s1 = config.getString(node, s);
		saveData.put(node, s1);
		return s1;
	}
	
	public List<String> getStringList(String node) {
		List<String> s = config.getStringList(node);
		saveData.put(node, s);
		return s;
	}
	
	public Vector get(String node, Vector v) {
		Vector v1 = config.getVector(node, v);
		saveData.put(node, v1);
		return v1;
	}
	
	public static void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf)) > 0){
	            out.write(buf, 0, len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
