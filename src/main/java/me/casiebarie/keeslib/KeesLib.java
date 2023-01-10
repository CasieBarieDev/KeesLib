package me.casiebarie.keeslib;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.security.Permission;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple library for my plugins!
 * @author CasieBarie
 * @version 1.0.1
 */
public class KeesLib {
	final JavaPlugin plugin;
	/**
	 * A simple library for my plugins!
	 * @param plugin Your main class that {@code extends} {@link JavaPlugin}.
	 */
	public KeesLib(@Nonnull JavaPlugin plugin) {this.plugin = plugin;}
	/**
	 * @return {@code true} when PlaceholderAPI is enabled in the server.
	 * @since 1.0.0
	 */
	public boolean hasPlaceholerAPI() {return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;}
	/**
	 * Checks if the server is legacy and disables the plugin if the version is not supported.
	 * @param lowestVersion The lowest supported minecraft version.
	 * @param highestVersion The highest supported minecraft version.
	 * @return {@code true} if the server has a legacy version.
	 * @since 1.0.0
	 */
	public boolean isLegacy2(@Nonnull BukkitVersions lowestVersion, @Nonnull BukkitVersions highestVersion) {
		String bukkitVersion = plugin.getServer().getClass().getPackage().getName().replace("org.bukkit.craftbukkit.","");
		int lowestVersionIndex = lowestVersion.ordinal();
		int highestVersionIndex = highestVersion.ordinal();
		BukkitVersions bukkitVersionE = null;
		for(BukkitVersions enumVersion : BukkitVersions.values()) {if(enumVersion.name().equals(bukkitVersion)) {bukkitVersionE = enumVersion;}}
		if(bukkitVersionE.ordinal() < lowestVersionIndex || bukkitVersionE.ordinal() > highestVersionIndex) {
			plugin.getLogger().log(Level.SEVERE, "Unsupported bukkit version! Disabling plugin...");
			Bukkit.getServer().getPluginManager().disablePlugin(plugin);
			return false;
		} return bukkitVersionE.isLegacy;
	}
	/**
	 * @param economy Set to {@code true} if you need Economy.
	 * @param permissions Set to {@code true} if you need Permissions.
	 * @param chat Set to {@code true} if you need Chat.
	 * @return {@code true} If <a href="https://www.spigotmc.org/resources/vault.34315/">Vault</a> is enabled and all the servicemanagers are registered.
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
	 * A fancy UpdateChecker to let server owners know when there is an new update.
	 * It shows the new update with a download link.
	 * @param recourceID Spigot recource ID
	 * @param borderColor Color of the fancy border.
	 * @param textColor Color of the text.
	 * @param character Character the border is made of.
	 * @param permission Players with this permission will receive a message when there is a new update.
	 * @param frequencyHours Check frequency in hours.
	 * @since 1.0.0
	 */
	public void updateChecker(@Nonnull Integer recourceID, @Nonnull ChatColor borderColor, @Nonnull ChatColor textColor, @Nonnull String character, @Nonnull String permission, @Nonnull Double frequencyHours) {
		new UpdateChecker(plugin, recourceID, borderColor, textColor, character, permission, frequencyHours).startChecking();
	}
	/**
	 * Translates all the {@code #HEX} codes in the message to {@link ChatColor} codes.
	 * @param msg Message to translate.
	 * @return Translated message.
	 * @since 1.0.0
	 */
	public String hex(@Nonnull String msg) {
		Pattern pattern = Pattern.compile("#[a-fA-F\\d]{6}");
		Matcher matcher = pattern.matcher(msg);
		while(matcher.find()) {
			String color = msg.substring(matcher.start(), matcher.end());
			msg = msg.replace(color, ChatColor.of(color) + "");
			matcher = pattern.matcher(msg);
		} return ChatColor.translateAlternateColorCodes('&', msg);
	}
}