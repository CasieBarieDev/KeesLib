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

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A flexible and customizable update checker for Bukkit/Spigot plugins.
 * <p>
 * This update checker allows plugin developers to easily check for new versions of their plugin
 * on SpigotMC. It provides a customizable format for update messages and allows developers
 * to set colors, borders, permissions and other display options.
 * </p>
 *
 * <h2>Features:</h2>
 * <ul>
 *     <li>Automatically checks for updates on SpigotMC.</li>
 *     <li>Customizable message formatting (colors, borders, and text styles).</li>
 *     <li>Supports permission-based update notifications.</li>
 *     <li>Configurable update check intervals.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * keeslib.updateChecker(12345) // Replace with actual resource ID
 *     .setBorderColor(ChatColor.GOLD)
 *     .setTextColor(ChatColor.YELLOW)
 *     .setUpdateFrequency(6L, TimeUnit.HOURS)
 *     .startChecking();
 * }</pre>
 *
 * @author CasieBarie
 * @version 1.3.1
 * @since 1.0.0
 */
public class UpdateChecker implements Listener {
	final JavaPlugin plugin;
	boolean firstCheck = true, hasNewVersion = false;
	Integer recourceID;
	String pluginName;
	ChatColor borderColor = ChatColor.GOLD, textColor = ChatColor.YELLOW, nameColor = ChatColor.DARK_AQUA, newVersionColor = ChatColor.GREEN, currentVersionColor = ChatColor.RED, urlColor = ChatColor.DARK_GRAY;
	Character character = '#';
	String permission = "", spigotVersion, lastCheckVersion, currentVersion;
	Long frequency = 1L;
	TimeUnit timeUnit = TimeUnit.HOURS;
	protected UpdateChecker(JavaPlugin plugin, Integer recourceID) {
		this.plugin = plugin;
		this.recourceID = recourceID;
		pluginName = plugin.getName();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Sets the plugin name to be displayed in the update messages.
	 * <p>Default: {@linkplain JavaPlugin#getName()}.</p>
	 *
	 * @param name the plugin name.
	 * @return this {@linkplain UpdateChecker} instance for chaining.
	 * @throws NullPointerException if name is null.
	 * @since 1.3.0
	 */
	public UpdateChecker setPluginName(@Nonnull String name) {this.pluginName = Objects.requireNonNull(name, "Name cannot be null"); return this;}

	/**
	 * Sets the border color for the update messages.
	 * <p>Default: {@linkplain ChatColor#GOLD}</p>
	 *
	 * @param color the {@linkplain ChatColor} to use for the message border.
	 * @return this {@linkplain UpdateChecker} instance for chaining.
	 * @throws NullPointerException if color is null.
	 * @since 1.1.0
	 */
	public UpdateChecker setBorderColor(@Nonnull ChatColor color) {this.borderColor = Objects.requireNonNull(color, "Color cannot be null"); return this;}

	/**
	 * Sets the primary text color for the update messages.
	 * <p>Default: {@linkplain ChatColor#YELLOW}</p>
	 *
	 * @param color the {@linkplain ChatColor} to use for message text.
	 * @return this {@linkplain UpdateChecker} instance for chaining.
	 * @throws NullPointerException if color is null.
	 * @since 1.1.0
	 */
	public UpdateChecker setTextColor(@Nonnull ChatColor color) {this.textColor = Objects.requireNonNull(color, "Color cannot be null"); return this;}

	/**
	 * Sets the color used for the plugin name in the update messages.
	 * <p>Default: {@linkplain ChatColor#DARK_AQUA}</p>
	 *
	 * @param color the {@linkplain ChatColor} to apply to the plugin name.
	 * @return this {@linkplain UpdateChecker} instance for chaining.
	 * @throws NullPointerException if color is null.
	 * @since 1.3.0
	 */
	public UpdateChecker setNameColor(@Nonnull ChatColor color) {this.nameColor = Objects.requireNonNull(color, "Color cannot be null"); return this;}

	/**
	 * Sets the color for displaying the new version number in the update messages.
	 * <p>Default: {@linkplain ChatColor#GREEN}</p>
	 *
	 * @param color the {@linkplain ChatColor} to apply to the new version.
	 * @return this {@linkplain UpdateChecker} instance for chaining.
	 * @throws NullPointerException if color is null.
	 * @since 1.3.0
	 */
	public UpdateChecker setNewVersionColor(@Nonnull ChatColor color) {this.newVersionColor = Objects.requireNonNull(color, "Color cannot be null"); return this;}

	/**
	 * Sets the color for displaying the current version number in the update messages.
	 * <p>Default: {@linkplain ChatColor#RED}</p>
	 *
	 * @param color the {@linkplain ChatColor} to apply to the current version.
	 * @return this {@linkplain UpdateChecker} instance for chaining.
	 * @throws NullPointerException if color is null.
	 * @since 1.3.0
	 */
	public UpdateChecker setCurrentVersionColor(@Nonnull ChatColor color) {this.currentVersionColor = Objects.requireNonNull(color, "Color cannot be null"); return this;}

	/**
	 * Sets the color used for the update URL in the update messages.
	 * <p>Default: {@linkplain ChatColor#DARK_GRAY}</p>
	 *
	 * @param color the {@linkplain ChatColor} to apply to the URL.
	 * @return this {@linkplain UpdateChecker} instance for chaining.
	 * @throws NullPointerException if color is null.
	 * @since 1.3.0
	 */
	public UpdateChecker setUrlColor(@Nonnull ChatColor color) {this.urlColor = Objects.requireNonNull(color, "Color cannot be null"); return this;}

	/**
	 * Sets the character used for the border in the update messages.
	 * <p>Default: {@code '#'}</p>
	 *
	 * @param character the border character.
	 * @return this {@linkplain UpdateChecker} instance for chaining.
	 * @throws NullPointerException if character is null.
	 * @since 1.0.0
	 */
	public UpdateChecker setBorderCharacter(@Nonnull Character character) {this.character = Objects.requireNonNull(character, "Character cannot be null"); return this;}

	/**
	 * Sets the permission required to view update messages ingame.
	 * <p>Default: An empty string ({@code ""}), meaning no players can see the update messages ingame.</p>
	 *
	 * @param permission the permission string.
	 * @return this {@linkplain UpdateChecker} instance for chaining.
	 * @throws NullPointerException if permission is null.
	 * @since 1.0.0
	 */
	public UpdateChecker setPermission(@Nonnull String permission) {this.permission = Objects.requireNonNull(permission, "Permission cannot be null"); return this;}

	/**
	 * Sets how frequently the update checker should run.
	 * <p>Default: {@code 1} {@linkplain TimeUnit#HOURS}.</p>
	 *
	 * @param frequency the update check interval.
	 * @param timeUnit  the time unit for the interval.
	 * @return this {@linkplain UpdateChecker} instance for chaining.
	 * @throws NullPointerException if frequency or timeUnit is null.
	 * @since 1.0.0
	 */
	public UpdateChecker setUpdateFrequency(@Nonnull Long frequency, @Nonnull TimeUnit timeUnit) {
		this.frequency = Objects.requireNonNull(frequency, "Frequency cannot be null");
		this.timeUnit = Objects.requireNonNull(timeUnit, "TimeUnit cannot be null");
		return this;
	}

	/**
	 * Starts the scheduled update checks based on the configurations.
	 * <p>
	 * This method starts an automatic task that periodically checks for new versions of the plugin
	 * on SpigotMC.
	 * </p>
	 * <p>
	 * Ensure that you call this method in your plugin's {@linkplain JavaPlugin#onEnable()} method to keep update
	 * checking active throughout the plugin's lifecycle.
	 * </p>
	 *
	 * @since 1.0.0
	 */
	public void startChecking() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			getSpigotVersion(v -> spigotVersion = v);
			if(spigotVersion == null) {return;}
			int spigotVersionInt = 0, currentVersionInt = 0;
			currentVersion = plugin.getDescription().getVersion();

			try {
				spigotVersionInt = Integer.parseInt(spigotVersion.replaceAll("\\.", ""));
				currentVersionInt = Integer.parseInt(currentVersion.replaceAll("\\.", ""));
			} catch(NumberFormatException e) {plugin.getLogger().warning("Failed to check for updates: " + e.getMessage());}

			if(spigotVersionInt == currentVersionInt) {
				if(firstCheck) {plugin.getLogger().info(String.format("You are using the most recent version. (v%s)", currentVersion));}
				firstCheck = false;
				return;
			}

			if(lastCheckVersion.equals(spigotVersion)) {return;}
			lastCheckVersion = spigotVersion;
			hasNewVersion = true;
			createMessage();
		}, 0L, TimeUnit.MILLISECONDS.convert(frequency, timeUnit));
	}

	private void getSpigotVersion(Consumer<String> consumer) {
		try(InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + recourceID).openStream(); Scanner scanner = new Scanner(is)) {
			if(scanner.hasNext()) {consumer.accept(scanner.next());}
		} catch(Exception e) {plugin.getLogger().warning("Failed to check for updates: " + e.getMessage());}
	}

	private void createMessage() {
		List<String> lines = new ArrayList<>();
		lines.add(String.format("There is a new version of §%s%s %savailable!", nameColor, plugin.getName(), textColor));
		lines.add("");
		lines.add(String.format("Current version: §%sv%s", currentVersionColor, currentVersion));
		lines.add(String.format("    New version: §%sv%s", newVersionColor, spigotVersion));
		lines.add("");
		lines.add("Please update to the newest version!");
		lines.add(String.format("Download: §%shttps://www.spigotmc.org/resources/%s", urlColor, recourceID));

		int longestLine = 0;
		for(String line : lines) {longestLine = Math.max(line.length(), longestLine);}
		longestLine += 4;
		if(longestLine > 120) {longestLine = 122;}

		StringBuilder builder = new StringBuilder(longestLine);
		Stream.generate(() -> character).limit(longestLine).forEach(builder::append);

		Bukkit.getConsoleSender().sendMessage(borderColor + builder.toString());
		for(String line : lines) {Bukkit.getConsoleSender().sendMessage(borderColor + character.toString() + " " + textColor + line);}
		Bukkit.getConsoleSender().sendMessage(borderColor + builder.toString());
	}

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if(!hasNewVersion || permission.isEmpty() || !player.hasPermission(permission)) {return;}
		String prefix = plugin.getDescription().getPrefix();

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			//noinspection deprecation
			player.spigot().sendMessage(new ComponentBuilder(prefix + " §7There is a new update available. (§av" + spigotVersion + "§7)\nDownload the new version on the ").color(ChatColor.GRAY)
				.append("Spigot").color(ChatColor.YELLOW).underlined(true)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("https://www.spigotmc.org/resources/" + recourceID).create()))
				.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/" + recourceID))
				.append(" page.").color(ChatColor.GRAY).underlined(false).event((HoverEvent) null).event((ClickEvent) null).create()
			);
		}, 40L);
	}
}