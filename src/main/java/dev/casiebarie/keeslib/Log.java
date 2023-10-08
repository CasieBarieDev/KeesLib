package dev.casiebarie.keeslib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log extends Logger {
	final String prefix;
	public Log(JavaPlugin plugin) {
		super(plugin.getName(), null);
		prefix = plugin.getDescription().getPrefix();
		setParent(plugin.getServer().getLogger());
		setLevel(Level.ALL);
	}

	@Override
	public void log(LogRecord record) {
		if(!record.getLevel().equals(Level.INFO)) {super.log(record); return;}
		Bukkit.getConsoleSender().sendMessage(prefix + "§r " + record.getMessage());
	}
}