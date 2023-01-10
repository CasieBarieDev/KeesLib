package me.casiebarie.keeslib;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class UpdateChecker implements Listener {
	final JavaPlugin plugin; final Integer recourceID; final ChatColor borderColor, textColor; final String character, permission; final Long updateFrequency;
	String spigotV, currV, lastCheckV = ""; Boolean firstCheck = true, hasNewV = false;
	private void send(String msg) {Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));}
	protected UpdateChecker(JavaPlugin plugin, Integer recourceID, ChatColor borderColor, ChatColor textColor, String character, String permission, Double freqencyHours) {
		this.plugin = plugin; this.recourceID = recourceID; this.borderColor = borderColor; this.textColor = textColor;
		this.character = character; this.permission = permission; this.updateFrequency = ((int)((freqencyHours * 60) * 60)) * 20L;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	protected void startChecking() {
		currV = plugin.getDescription().getVersion();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			getSpigotVersion(version -> spigotV = version);
			if(spigotV == null) {return;}
			int spigotVint = 0, currVint = 0;
			try {
				spigotVint = Integer.parseInt(spigotV.replaceAll("\\.", ""));
				currVint = Integer.parseInt(currV.replaceAll("\\.", ""));
			} catch (NumberFormatException e) {plugin.getLogger().info("Failed to check for updates: " + e.getMessage());}
			if((currVint == spigotVint)) {if(firstCheck) {plugin.getLogger().info(String.format("You are using the most recent version. (v%s)", currV)); firstCheck = false;} return;}
			if(lastCheckV.equals(spigotV)) {return;}
			lastCheckV = spigotV;
			List<String> lines = new ArrayList<>();
			boolean b = (currVint > spigotVint);
			hasNewV = true;
			lines.add((b) ? String.format("You are using the beta version of &b%s%s!", plugin.getName(), textColor) : String.format("There is a new version of &b%s %savailable!", plugin.getName(), textColor));
			lines.add("");
			lines.add(String.format("Current version: &6v%s", currV));
			lines.add((b) ? String.format(" Public version: &av%s", spigotV) : String.format("    New version: &av%s", spigotV));
			lines.add("");
			lines.add((b) ? "Please note that this version is not officially released!" : "Please update to the newest version!");
			lines.add(String.format("Download: &8https://www.spigotmc.org/resources/%s", recourceID));
			printToConsole(lines);
		}, 0L, updateFrequency);
	}

	private void getSpigotVersion(final Consumer<String> consumer) {
		try(InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + recourceID).openStream(); Scanner scanner = new Scanner(inputStream)) {
			if(scanner.hasNext()) {consumer.accept(scanner.next());}
		} catch (Exception e) {plugin.getLogger().info("Failed to check for updates: " + e.getMessage());}
	}

	private void printToConsole(List<String> lines) {
		int longestLine = 0;
		for(String line : lines) {longestLine = Math.max(line.length(), longestLine);}
		longestLine += 4;
		if(longestLine > 120) {longestLine = 122;}
		StringBuilder builder = new StringBuilder(longestLine);
		Stream.generate(() -> character).limit(longestLine).forEach(builder::append);
		send(borderColor + builder.toString());
		for(String line : lines) {send(borderColor + character + " " + textColor + line);}
		send(borderColor + builder.toString());
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			Player player = e.getPlayer();
			if(!(hasNewV && (player.hasPermission(permission) || permission == null))) {return;}
			player.spigot().sendMessage(new ComponentBuilder("[").color(ChatColor.GOLD).append(plugin.getName()).color(ChatColor.AQUA).append("] ").color(ChatColor.GOLD)
				.append("There is a new update available. (").color(ChatColor.GRAY)
				.append("v" + spigotV).color(ChatColor.GREEN)
				.append(") Download the new version on the ").color(ChatColor.GRAY)
				.append("Spigot").color(ChatColor.YELLOW).underlined(true)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("https://www.spigotmc.org/resources/" + recourceID).color(ChatColor.GRAY).create()))
				.event(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/" + recourceID))
				.append(" page.").color(ChatColor.GRAY).underlined(false).event((HoverEvent) null).event((ClickEvent) null).create());
		}, 40L);
	}
}