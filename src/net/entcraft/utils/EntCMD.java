package net.entcraft.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class EntCMD implements CommandExecutor, IEntCommandHandler {
	
	private JavaPlugin plugin;
	private String command;
	private String parentPermission = "*";
	private List<EntCMDHandler> subCMDs = new ArrayList<EntCMDHandler>();
	private HashMap<String, IEntCommandHandler> cmdHandlers = new HashMap<String, IEntCommandHandler>();
	private HashMap<String, String> permissions = new HashMap<String, String>();
	
	public EntCMD(JavaPlugin instance, String cmd) {
		plugin = instance;
		command = cmd;
		plugin.getCommand(command).setExecutor(this);
		this.addSubCommand("help", this, "<page>", 0);
	}
	
	public EntCMD(JavaPlugin instance, String cmd, String pPermission) {
		this(instance, cmd);
		parentPermission = pPermission;
	}
	
	/**
	 * Add a new sub command after the parent command e.g. everything after /command
	 * 
	 * @param label Used to pair commands together
	 * @param handler The command handler for this command
	 * @param subCMD the args of the command e.g. everything after the actual command "add points argNeeded secondArgNeeded"
	 * @param neededArgsIndex Index of the argument you need to process the command. Same example from above: 2, 3
	 */
	public void addSubCommand(String label, IEntCommandHandler handler, String subCMD, int... neededArgsIndex) {
		subCMDs.add(new EntCMDHandler(label, subCMD, neededArgsIndex));
		cmdHandlers.put(label, handler);
		sortList();
	}
	
	/**
	 * Add a new sub command after the parent command e.g. everything after /command
	 * 
	 * @param label Used to pair commands together
	 * @param handler The command handler for this command
	 * @param subCMD the args of the command e.g. everything after the actual command "add points argNeeded secondArgNeeded"
	 */
	public void addSubCommand(String label, IEntCommandHandler handler, String subCMD) {
		subCMDs.add(new EntCMDHandler(label, subCMD, null));
		cmdHandlers.put(label, handler);
		sortList();
	}
	
	/**
	 * Add a new sub command after the parent command e.g. everything after /command
	 * 
	 * @param label Used to pair commands together
	 * @param handler The command handler for this command
	 * @param subCMD the args of the command e.g. everything after the actual command "add points argNeeded secondArgNeeded"
	 * @param permission Permission needed to perform this command
	 * @param neededArgsIndex Index of the argument you need to process the command. Same example from above: 2, 3
	 */
	public void addSubCommand(String label, IEntCommandHandler handler, String subCMD, String permission, int... neededArgsIndex) {
		this.addSubCommand(label, handler, subCMD, neededArgsIndex);
		permissions.put(label, permission);
	}
	
	/**
	 * Add a new sub command after the parent command e.g. everything after /command
	 * 
	 * @param label Used to pair commands together
	 * @param handler The command handler for this command
	 * @param subCMD the args of the command e.g. everything after the actual command "add points argNeeded secondArgNeeded"
	 */
	public void addSubCommand(String label, IEntCommandHandler handler, String subCMD, String permission) {
		this.addSubCommand(label, handler, subCMD);
		permissions.put(label, permission);
	}
	
	private void sortList() {
		
		Iterator<EntCMDHandler> iter = subCMDs.iterator();
		HashMap<Integer, List<EntCMDHandler>> hmc = new HashMap<Integer, List<EntCMDHandler>>();
		int highestArgCount = 0;
		
		// Sort by arguments length by storing in seperate lists
		while (iter.hasNext()) {
			EntCMDHandler c = iter.next();
			int length = c.sCMD.length;
			if (length > highestArgCount) highestArgCount = length;
			if (hmc.containsKey(length)) {
				hmc.get(length).add(c);
			} else {
				hmc.put(length, new ArrayList<EntCMDHandler>());
				hmc.get(length).add(c);
			}
		}
		
		// Sort each seperate list by the least amount of needed args first
		// Then add those back to original list
		subCMDs.clear();
		for (int i = highestArgCount; i >= 1; i--) {
			List<EntCMDHandler> cList = hmc.get(i);
			Collections.sort(cList, new Comparator<EntCMDHandler>() {
				@Override
				public int compare(EntCMDHandler o1, EntCMDHandler o2) {
					return (o1.neededArg == null ? 0 : o1.neededArg.length) - (o2.neededArg == null ? 0 : o2.neededArg.length);
				}
			});
			subCMDs.addAll(cList);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) return displayHelp(sender, 1);
		for (int i = 0; i < subCMDs.size(); i++) {
			EntCMDHandler c = subCMDs.get(i);
			if (c.isCommand(args)) {
				if (c.hasPermission(sender)) {
					return cmdHandlers.get(c.getLabel()).processCommand(sender, c.getNeededArgs(args));
				} else {
					sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean processCommand(CommandSender sender, String[] neededArgs) {
		
		int page;
		if (neededArgs == null) {
			page = 1;
		} else {
			if (isInteger(sender, neededArgs[0])) {
				page = Integer.parseInt(neededArgs[0]);
			} else {
				return true;
			}
		}
		
		return displayHelp(sender, page);
	}
	
	@Override
	public String getDescription() {
		return "Lists Commands";
	}
	
	public boolean displayHelp(CommandSender sender, int page) {
		int maxPages = subCMDs.size() / 6 + (subCMDs.size() % 6 == 0 ? 0 : 1);
		if (page > maxPages) {
			page = maxPages;
		} else if (page < 1) {
			page = 1;
		}
		
		int startIndex = (page - 1) * 6;
		int endIndex = page * 6 - 1 - (page == maxPages ? 6 - (subCMDs.size() % 6) : 0);
		sender.sendMessage(ChatColor.GREEN + "===== /" + command + " Commands | Page " + page + " of " + maxPages + " =====");
		for (int i = startIndex; i <= endIndex; i++) {
			EntCMDHandler c = subCMDs.get(i);
			String desc = "/" + command + " " + c.getOriginalSubCommand() + " - " + cmdHandlers.get(c.getLabel()).getDescription();
			if (desc.length() > 45) {
				desc = desc.substring(0, 44) + "...";
			}
			if (c.hasPermission(sender)) {
				sender.sendMessage(ChatColor.GREEN + desc);
			} else {
				sender.sendMessage(ChatColor.RED + desc);
			}
		}
		
		return true;
	}
	
	private boolean isInteger(CommandSender sender, String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
//			sender.sendMessage(ChatColor.RED + "Error NumberFormatException: Incorrect value entered.");
			return false;
		}
		return true;
	}
	
	private class EntCMDHandler {
		
		private String label;
		private String sCMDOriginal;
		private String[] sCMD;
		private int[] neededArg;
		
		public EntCMDHandler(String theLabel, String subcmd, int[] narg) {
			label = theLabel;
			sCMDOriginal = subcmd;
			sCMD = subcmd.split(" ");
			neededArg = narg;
		}
		
		public boolean isCommand(String[] args) {
			if (args.length == sCMD.length) {
				for (int i = 0; i < sCMD.length; i++) {
					if (!args[i].equalsIgnoreCase(sCMD[i]) && !isANeededArg(i)) {
						return false;
					}
				}
			} else {
				return false;
			}
			return true;
		}
		
		private boolean isANeededArg(int i) {
			if (neededArg == null) return false;
			for (int k = 0; k < neededArg.length; k++) {
				if (neededArg[k] == i) {
					return true;
				}
			}
			return false;
		}
		
		public String[] getNeededArgs(String[] args) {
			if (neededArg == null) return null;
			String[] need = new String[neededArg.length];
			for (int i = 0; i < neededArg.length; i++) {
				need[i] = args[neededArg[i]];
			}
			return need;
		}
		
		public String getLabel() {
			return label;
		}
		
		public String getOriginalSubCommand() {
			return sCMDOriginal;
		}
		
		public boolean hasPermission(CommandSender sender) {
			return (sender.isOp() || sender.hasPermission(parentPermission) 
					|| !permissions.containsKey(getLabel()) || sender.hasPermission(permissions.get(getLabel())));
		}
		
	}

}
