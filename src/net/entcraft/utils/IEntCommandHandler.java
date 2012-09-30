package net.entcraft.utils;

import org.bukkit.command.CommandSender;

public interface IEntCommandHandler {
	
	public abstract boolean processCommand(CommandSender sender, String[] neededArgs);

}
