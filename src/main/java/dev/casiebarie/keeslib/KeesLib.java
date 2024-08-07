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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * A simple library for my plugins!
 * @author CasieBarie
 * @version 1.3.0
 */
public class KeesLib {
	final JavaPlugin plugin;
	/**
	 * A simple library for my plugins!
	 * @param plugin Your main class that {@code extends} {@linkplain org.bukkit.plugin.java.JavaPlugin}.
	 */
	public KeesLib(@Nonnull JavaPlugin plugin) {this.plugin = plugin;}
	/**
	 * @return {@code true} when <a href=https://www.spigotmc.org/resources/placeholderapi.6245/>PlaceholderAPI</a> is enabled in the server.
	 * @since 1.0.0
	 */
	public boolean hasPlaceholerAPI() {return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;}
	/**
	 * Checks if the server is legacy and disables the plugin if the lowest version is not supported.
	 * @param lowestVersion The lowest supported minecraft version (E.g. 1.8)
	 * @param highestVersion The highest tested minecraft version.
	 * @return {@code true} if the server has a legacy version.
	 * @since 1.0.0
	 */
	public boolean isLegacy(@Nonnull Double lowestVersion, Double highestVersion) {
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
	 * @param economy Set to {@code true} if you need Economy.
	 * @param permissions Set to {@code true} if you need Permissions.
	 * @param chat Set to {@code true} if you need Chat.
	 * @return {@code true} if <a href="https://www.spigotmc.org/resources/vault.34315/">Vault</a> is enabled and all the servicemanagers are registered.
	 * @since 1.0.0
	 */
	public boolean hasVault(@Nonnull Boolean economy, @Nonnull Boolean permissions, @Nonnull Boolean chat) {
		if(plugin.getServer().getPluginManager().getPlugin("Vault") == null) {return false;}
		ArrayList<RegisteredServiceProvider> providers = new ArrayList<>();
		if(economy) {providers.add(plugin.getServer().getServicesManager().getRegistration(Economy.class));}
		if(permissions) {providers.add(plugin.getServer().getServicesManager().getRegistration(Permission.class));}
		if(chat) {providers.add(plugin.getServer().getServicesManager().getRegistration(Chat.class));}
		return !providers.contains(null);
	}
	/**
	 * Translates all the {@code #HEX} codes in the message to {@linkplain net.md_5.bungee.api.ChatColor} codes.
	 * @param msg Message to translate.
	 * @return Translated message.
	 * @since 1.0.0
	 */
	public String hex(@Nonnull String msg) {
		Pattern pattern = Pattern.compile("#[a-fA-F\\d]{6}");
		Matcher matcher = pattern.matcher(msg);
		while(matcher.find()) {
			String color = msg.substring(matcher.start(), matcher.end());
			msg = msg.replace(color, String.valueOf(ChatColor.of(color)));
			matcher = pattern.matcher(msg);
		} return ChatColor.translateAlternateColorCodes('&', msg);
	}
	/**
	 * Rounds the specified number and returns it as a {@linkplain String}. (E.g. 3748 to 3.7K)
	 * @param number The number to round.
	 * @return The fromatted String.
	 * @since 1.2.0
	 */
	public String roundNumber(@Nonnull Number number) {
		if(number == null) {return "";}
		long numberLong = number.longValue();
		if(numberLong < 1000) return "" + numberLong;
		int exp = (int) (Math.log(numberLong) / Math.log(1000));
		return String.format("%.1f%c", numberLong / Math.pow(1000, exp), "kMGTPE".charAt(exp-1));
	}
	/**
	 * Changes the prefix of the plugin logger to the value {@code Prefix:} in the {@code plugin.yml} with {@linkplain net.md_5.bungee.api.ChatColor} support.
	 * @return a new instance of {@linkplain Logger}.
	 * @since 1.1.0
	 */
	public Logger createLogger() {return new Log(plugin);}
	/**
	 * Fancy updatechecker.
	 * @param resourceID Recource ID of Spigot.
	 * @return a new reference to the {@linkplain UpdateChecker}.
	 * @since 1.0.0
	 */
	public UpdateChecker updateChecker(@Nonnull Integer resourceID) {return new UpdateChecker(resourceID);}
 	public class UpdateChecker {
		protected Integer recourceID;
		protected String pluginName = plugin.getName();
		protected ChatColor
			borderColor = ChatColor.GOLD,
			textColor = ChatColor.YELLOW,
			nameColor = ChatColor.DARK_AQUA,
			newVersionColor = ChatColor.GREEN,
			currentVersionColor = ChatColor.RED,
			urlColor = ChatColor.DARK_GRAY;
		protected Character character = '#';
		protected String permission = "";
		protected Long frequency = 1L;
		protected TimeUnit timeUnit = TimeUnit.HOURS;
		/**
		 * Sets the plugin name displayed in the message.
		 * @param name Default: {@linkplain org.bukkit.plugin.java.JavaPlugin#getName()}.
		 * @return a reference to this object for easy chaining.
		 * @since 1.3.0
		 */
		public UpdateChecker setPluginName(@Nonnull String name) {this.pluginName = name; return this;}
		/**
		 * Sets the border color of the message.
		 * @param color Default: {@linkplain net.md_5.bungee.api.ChatColor#GOLD}.
		 * @return a reference to this object for easy chaining.
		 * @since 1.1.0
		 */
		public UpdateChecker setBorderColor(@Nonnull ChatColor color) {borderColor = color; return this;}
		/**
		 * Sets the text color of the message.
		 * @param color Default: {@linkplain net.md_5.bungee.api.ChatColor#YELLOW}.
		 * @return a reference to this object.
		 * @since 1.1.0
		 */
		public UpdateChecker setTextColor(@Nonnull ChatColor color) {textColor = color; return this;}
		/**
		 * Sets the color of the plugin name in the message.
		 * @param color Default: {@linkplain net.md_5.bungee.api.ChatColor#AQUA}.
		 * @return a reference to this object.
		 * @since 1.3.0
		 */
		public UpdateChecker setNameColor(@Nonnull ChatColor color) {nameColor = color; return this;}
		/**
		 * Sets the color of the new version.
		 * @param color Default: {@linkplain net.md_5.bungee.api.ChatColor#GREEN}.
		 * @return a reference to this object.
		 * @since 1.3.0
		 */
		public UpdateChecker setNewVersionColor(@Nonnull ChatColor color) {newVersionColor = color; return this;}
		/**
		 * Sets the color of the current version.
		 * @param color Default: {@linkplain net.md_5.bungee.api.ChatColor#RED}.
		 * @return a reference to this object.
		 * @since 1.3.0
		 */
		public UpdateChecker setCurrentVersionColor(@Nonnull ChatColor color) {currentVersionColor = color; return this;}
		/**
		 * Sets the color of the download url.
		 * @param color Default: {@linkplain net.md_5.bungee.api.ChatColor#DARK_GRAY}.
		 * @return a reference to this object.
		 * @since 1.3.0
		 */
		public UpdateChecker setUrlColor(@Nonnull ChatColor color) {urlColor = color; return this;}
		/**
		 * Sets the bordor character surrounding the message.
		 * @param character Default: {@code #}.
		 * @return a reference to this object.
		 * @since 1.1.0
		 */
		public UpdateChecker setBorderCharacter(@Nonnull Character character) {this.character = character; return this;}
		/**
		 * The permission a player must have to receive the update notice ingame.
		 * @param permission Default: {@code ""}.
		 * @return a reference to this object.
		 * @since 1.1.0
		 */
		public UpdateChecker setPermission(@Nonnull String permission) {this.permission = permission; return this;}
		/**
		 * Sets the frequency of checks.
		 * @param frequency Default: {@code 1L}.
		 * @param timeUnit Default: {@code TimeUnit.HOURS}.
		 * @return a reference to this object.
		 * @since 1.1.0
		 */
		public UpdateChecker setUpdateFrequency(@Nonnull Long frequency, @Nonnull TimeUnit timeUnit) {this.frequency = frequency; this.timeUnit = timeUnit; return this;}
		public UpdateChecker(Integer recourceID) {this.recourceID = recourceID;}
		/**
		 * Starts the checking!
		 */
		public void startChecking() {new dev.casiebarie.keeslib.UpdateChecker(plugin, this).start();}
	}
}