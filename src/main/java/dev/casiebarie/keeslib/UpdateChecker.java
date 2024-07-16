package dev.casiebarie.keeslib;

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
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class UpdateChecker implements Listener {
	final JavaPlugin plugin;
	final KeesLib.UpdateChecker values;
	String spigotVersion, lastCheckVersion, currentVersion;
	boolean firstCheck = true, hasNewVersion = false;
	protected UpdateChecker(JavaPlugin plugin, KeesLib.UpdateChecker values) {
		this.plugin = plugin;
		this.values = values;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	protected void start() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			getSpigotVersion(v -> spigotVersion = v);
			if(spigotVersion == null) {return;}
			int spigotVersionInt = 0, currentVersionInt = 0;
			currentVersion = plugin.getDescription().getVersion();

			try {
				spigotVersionInt = Integer.parseInt(spigotVersion.replaceAll("\\.", ""));
				currentVersionInt = Integer.parseInt(currentVersion.replaceAll("\\.", ""));
			} catch (NumberFormatException e) {plugin.getLogger().warning("Failed to check for updates: " + e.getMessage());}

			if(spigotVersionInt == currentVersionInt) {
				if(firstCheck) {
					plugin.getLogger().info(String.format("You are using the most recent version. (v%s)", currentVersion));
				} firstCheck = false;
				return;
			}

			if(lastCheckVersion.equals(spigotVersion)) {return;}
			lastCheckVersion = spigotVersion;
			hasNewVersion = true;
			createMessage();
		}, 0L, TimeUnit.MILLISECONDS.convert(values.frequency, values.timeUnit));
	}

	private void createMessage() {
		List<String> lines = new ArrayList<>();
		lines.add(String.format("There is a new version of §%s%s %savailable!", values.nameColor, plugin.getName(), values.textColor));
		lines.add("");
		lines.add(String.format("Current version: §%sv%s", values.currentVersionColor, currentVersion));
		lines.add(String.format("    New version: §%sv%s", values.newVersionColor, spigotVersion));
		lines.add("");
		lines.add("Please update to the newest version!");
		lines.add(String.format("Download: §%shttps://www.spigotmc.org/resources/%s", values.urlColor, values.recourceID));

		int longestLine = 0;
		for(String line : lines) {longestLine = Math.max(line.length(), longestLine);}
		longestLine += 4;
		if(longestLine > 120) {longestLine = 122;}

		StringBuilder builder = new StringBuilder(longestLine);
		Stream.generate(() -> values.character).limit(longestLine).forEach(builder::append);

		send(values.borderColor + builder.toString());
		for(String line : lines) {send(values.borderColor + values.character.toString() + " " + values.textColor + line);}
		send(values.borderColor + builder.toString());
	}

	private void getSpigotVersion(Consumer<String> consumer) {
		try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + values.recourceID).openStream(); Scanner scanner = new Scanner(is)) {
			if(scanner.hasNext()) {consumer.accept(scanner.next());}
		} catch (Exception e) {plugin.getLogger().warning("Failed to check for updates: " + e.getMessage());}
	}

	private void send(String msg) {Bukkit.getConsoleSender().sendMessage(msg);}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			Player player = e.getPlayer();
			if(!(hasNewVersion && (player.hasPermission(values.permission) || values.permission == null))) {return;}
			String prefix = plugin.getDescription().getPrefix();

			//noinspection deprecation
			player.spigot().sendMessage(new ComponentBuilder(prefix + " §7There is a new update available. (§av" + spigotVersion + "§7)\nDownload the new version on the ").color(ChatColor.GRAY)
				.append("Spigot").color(ChatColor.YELLOW).underlined(true)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("https://www.spigotmc.org/resources/" + values.recourceID).create()))
				.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/" + values.recourceID))
				.append(" page.").color(ChatColor.GRAY).underlined(false).event((HoverEvent) null).event((ClickEvent) null).create()
			);
		}, 40L);
	}
}