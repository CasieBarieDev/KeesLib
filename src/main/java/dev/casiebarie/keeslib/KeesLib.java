package dev.casiebarie.keeslib;

import com.google.common.primitives.Doubles;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A lightweight utility library for use in my plugins.
 * <p>
 * This library provides common utilities and helper methods to simplify plugin
 * development. It is designed to be easily integrated into Bukkit/Spigot plugins.
 * </p>
 *
 * @author CasieBarie
 * @version 1.3.1
 */
public class KeesLib {
	final JavaPlugin plugin;

	/**
	 * Initializes the library with the given plugin instance.
	 *
	 * @param plugin the main plugin class that {@code extends} {@linkplain JavaPlugin}.
	 * @throws NullPointerException if the provided plugin is null.
	 */
	public KeesLib(@Nonnull JavaPlugin plugin) {this.plugin = Objects.requireNonNull(plugin, "Plugin cannot be null");}

	/**
	 * Checks if <a href="https://www.spigotmc.org/resources/placeholderapi.6245/">PlaceholderAPI</a> is enabled on the server.
	 *
	 * @return {@code true} if PlaceholderAPI is enabled, {@code false} otherwise.
	 * @since 1.0.0
	 */
	public boolean hasPlaceholerAPI() {return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;}

	/**
	 * Checks if the server is running a legacy version of Minecraft and disables the plugin
	 * if the server version is lower than the lowest supported version.
	 *
	 * @param lowestVersion the lowest supported Minecraft version (e.g., 1.8).
	 * @param highestVersion the highest tested Minecraft version.
	 * @return {@code true} if the server is running a legacy version, {@code false} otherwise.
	 * @throws NullPointerException if the lowestVersion is null.
	 * @since 1.0.0
	 */
	public boolean isLegacy(@Nonnull Double lowestVersion, Double highestVersion) {
		lowestVersion = Objects.requireNonNull(lowestVersion, "Lowest version cannot be null");
		double bukkitVersion = Double.parseDouble(Bukkit.getBukkitVersion().split("-")[0]);
		if(bukkitVersion < lowestVersion) {
			plugin.getLogger().log(Level.SEVERE, "Unsupported " + Bukkit.getName() + " version! Disabling plugin...");
			Bukkit.getServer().getPluginManager().disablePlugin(plugin);
			return false;
		} if(highestVersion != null) {if(bukkitVersion > highestVersion) {plugin.getLogger().warning("This version of " + Bukkit.getName() + " is not officially supported! Proceed with caution!");}}
		double[] legacyVersions = {1.8, 1.9, 1.10, 1.11, 1.12};
		return (Doubles.contains(legacyVersions, bukkitVersion));
	}

	/**
	 * Checks if <a href="https://www.spigotmc.org/resources/vault.34315/">Vault</a> is enabled
	 * and whether the specified service managers (Economy, Permissions, and Chat) are registered.
	 *
	 * @param economy {@code true} if Economy support is required.
	 * @param permissions {@code true} if Permissions support is required.
	 * @param chat {@code true} if Chat support is required.
	 * @return {@code true} if Vault is enabled and all requested service managers are registered, {@code false} otherwise.
	 * @since 1.0.0
	 */
	public boolean hasVault(boolean economy, boolean permissions, boolean chat) {
		if(plugin.getServer().getPluginManager().getPlugin("Vault") == null) {return false;}
		ArrayList<RegisteredServiceProvider> providers = new ArrayList<>();
		if(economy) {providers.add(plugin.getServer().getServicesManager().getRegistration(Economy.class));}
		if(permissions) {providers.add(plugin.getServer().getServicesManager().getRegistration(Permission.class));}
		if(chat) {providers.add(plugin.getServer().getServicesManager().getRegistration(Chat.class));}
		return !providers.contains(null);
	}

	/**
	 * Translates all {@code #HEX} color codes in the given message into
	 * {@linkplain ChatColor} formatted colors.
	 * <p>
	 * This method replaces occurrences of {@code #RRGGBB} in the message with their
	 * corresponding Minecraft color codes, allowing for full RGB color support.
	 * </p>
	 *
	 * @param msg the message containing {@code #HEX} color codes to be translated.
	 * @return the message with translated color codes.
	 * @throws NullPointerException if the provided message is null.
	 * @since 1.0.0
	 */
	public String hex(@Nonnull String msg) {
		msg = Objects.requireNonNull(msg, "Message cannot be null");
		Pattern pattern = Pattern.compile("#[a-fA-F\\d]{6}");
		Matcher matcher = pattern.matcher(msg);
		while(matcher.find()) {
			String color = msg.substring(matcher.start(), matcher.end());
			msg = msg.replace(color, String.valueOf(ChatColor.of(color)));
			matcher = pattern.matcher(msg);
		} return ChatColor.translateAlternateColorCodes('&', msg);
	}

	/**
	 * Rounds the specified number and returns it as a formatted {@linkplain String}.
	 * <p>
	 * This method shortens large numbers using suffixes such as k (thousand), M (million),
	 * and B (billion). For example, {@code 3748} is converted to {@code 3.7k}.
	 * </p>
	 *
	 * @param number the number to round and format.
	 * @return the formatted string representation of the number.
	 * @throws NullPointerException if the provided number is null.
	 * @since 1.2.0
	 */
	public String roundNumber(@Nonnull Number number) {
		Objects.requireNonNull(number, "Number cannot be null");
		long numberLong = number.longValue();
		if(numberLong < 1000) return "" + numberLong;
		int exp = (int) (Math.log(numberLong) / Math.log(1000));
		return String.format("%.1f%c", numberLong / Math.pow(1000, exp), "kMGTPE".charAt(exp-1));
	}

	/**
	 * Creates a new instance of {@linkplain Logger} with a custom prefix.
	 * <p>
	 * The prefix is set to the value of {@code Prefix:} defined in the {@code plugin.yml}.
	 * This method also supports {@linkplain net.md_5.bungee.api.ChatColor} for colored logging.
	 * </p>
	 * <p>
	 * To use this logger as the default plugin logger, override {@linkplain JavaPlugin#getLogger()}:
	 * </p>
	 * <pre>
	 * {@code
	 * Logger logger = keesLib.createLogger();
	 *
	 * @Override
	 * public @NotNull Logger getLogger() {
	 *     return logger;
	 * }
	 * }
	 * </pre>
	 *
	 * @return a new instance of {@linkplain Logger} with the specified prefix.
	 * @since 1.1.0
	 */
	public Logger createLogger() {return new Log(plugin);}

	/**
	 * Creates a new instance of {@linkplain UpdateChecker} to check for updates on SpigotMC.
	 * <p>
	 * This method initializes an update checker using the specified SpigotMC resource ID.
	 * It allows plugins to fetch the latest version available on the platform.
	 * </p>
	 *
	 * @param resourceID the unique ID of the plugin's resource on SpigotMC.
	 * @return a new instance of {@linkplain UpdateChecker} for chaining.
	 * @throws NullPointerException if the provided resourceID is null.
	 * @since 1.0.0
	 */
	public UpdateChecker updateChecker(@Nonnull Integer resourceID) {return new UpdateChecker(plugin, Objects.requireNonNull(resourceID, "ResourceID cannot be null"));}
}