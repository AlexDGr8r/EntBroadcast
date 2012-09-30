package net.entcraft.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class EntCMD implements CommandExecutor {
	
	private JavaPlugin plugin;
	private String command;
	private List<EntCMDHandler> subCMDs = new ArrayList<EntCMDHandler>();
	private HashMap<String, IEntCommandHandler> cmdHandlers = new HashMap<String, IEntCommandHandler>();
	
	public EntCMD(JavaPlugin instance, String cmd) {
		plugin = instance;
		command = cmd;
		plugin.getCommand(command).setExecutor(this);
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
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Iterator<EntCMDHandler> iter = subCMDs.iterator();
		while (iter.hasNext()) {
			EntCMDHandler c = iter.next();
			if (c.isCommand(args)) {
				cmdHandlers.get(c.getLabel()).processCommand(sender, c.getNeededArgs(args));
			}
		}
		return false;
	}
	
	private class EntCMDHandler {
		
		private String label;
		private String[] sCMD;
		private int[] neededArg;
		
		public EntCMDHandler(String theLabel, String subcmd, int[] narg) {
			label = theLabel;
			sCMD = subcmd.split(" ");
			neededArg = narg;
		}
		
		public boolean isCommand(String[] args) {
			if (args.length == sCMD.length) {
				for (int i = 0; i < sCMD.length; i++) {
					if (args[i] != sCMD[i] && !isANeededArg(i)) {
						return false;
					}
				}
			} else {
				return false;
			}
			return true;
		}
		
		private boolean isANeededArg(int i) {
			for (int k = 0; k < neededArg.length; k++) {
				if (neededArg[k] == i) {
					return true;
				}
			}
			return false;
		}
		
		public String[] getNeededArgs(String[] args) {
			String[] need = new String[neededArg.length];
			for (int i = 0; i < neededArg.length; i++) {
				need[i] = args[neededArg[i]];
			}
			return need;
		}
		
		public String getLabel() {
			return label;
		}
		
	}

}
