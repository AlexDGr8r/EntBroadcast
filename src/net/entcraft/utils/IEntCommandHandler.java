package net.entcraft.utils;

import org.bukkit.command.CommandSender;

public interface IEntCommandHandler {
	
	/**
	 * Processes given command. You do not need to check arguments or command permissions.
	 * 
	 * @param sender Player or Console who is sending the command
	 * @param neededArgs Array of needed arguments from Command. If no arguments we're needed, will be null.
	 * @return true if command was processed. false if it wasn't.
	 */
	public abstract boolean processCommand(CommandSender sender, String[] neededArgs);

}
