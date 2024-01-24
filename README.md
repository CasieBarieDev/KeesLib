# KeesLib
<p><a href="https://www.casiebarie.dev/Plugins/KeesLib"><img alt="Website" src="https://img.shields.io/static/v1?label=Website&message=KeesLib&color=yellow&logo=dev.to"></a> <a href="https://github.com/CasieBarieDev/KeesLib/releases/latest"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/CasieBarieDev/KeesLib?label=Release&logo=github"></a> <img alt="GitHub code size in bytes" src="https://img.shields.io/github/languages/code-size/CasieBarieDev/KeesLib?color=red&label=Size&logo=Github"> <a href="https://github.com/CasieBarieDev/KeesLib/blob/main/LICENSE"><img alt="GitHub" src="https://img.shields.io/github/license/CasieBarieDev/KeesLib?label=License&logo=Github"></a> <a href="https://jitpack.io/#dev.casiebarie/keeslib"><img alt="JitPack" src="https://img.shields.io/jitpack/version/dev.casiebarie/keeslib?color=lime&label=JitPack&logo=azurepipelines"></a></p>
<br/>

A simple library for my plugins!  

<br/>

## Patch Notes:
### [v1.2.0](https://github.com/CasieBarieDev/KeesLib/releases/tag/1.2.0)
- **Added** | `roundNumber(Number number)` - Rounds the specified number and returns it as a String. (E.g. 3748 -> 3.7K)
- **Fix** | A '&' was not a '§' in the UpdateChecker.
- **Typo** | A @since version was the wrong version in the javadoc.
- **Optimization** | Changed some objects to primitives.

### [v1.1.0](https://github.com/CasieBarieDev/KeesLib/releases/tag/1.1.0)
- **Added** | A new builder for the UpdateChecker.
- **Added** | `createLogger()` - Changes the prefix of the plugin logger to the value 'Prefix:' in the plugin.yml with ChatColor support.
- **Changed** | `isLegacy(Double lowestVerion, Double highestVersion)` - This will no longer use the BukkitVersions enum and no longer shuts down the plugin when the version is too high.
- **Removed** | `BukkitVersions` enum.

### [v1.0.2](https://github.com/CasieBarieDev/KeesLib/releases/tag/1.0.2)
- **Added** | Javadocs

### [v1.0.1](https://github.com/CasieBarieDev/KeesLib/releases/tag/1.0.1)
- **Fix** | Build on Jitpack

### [v1.0.0](https://github.com/CasieBarieDev/KeesLib/releases/tag/1.0.0) _(Initial release)_
- **Added** | `hasPlaceholerAPI()` | Returns true if [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245) is installed.
- **Added** | `isLegacy(BukkitVersions lowestVersion, BukkitVersions highestVersion)` | Checks if the server is legacy and disables the plugin if the version is not supported.
- **Added** | `hasVault(Boolean economy, Boolean permissions, Boolean chat)` | Checks if [Vault](https://www.spigotmc.org/resources/vault.34315/) and the servicemanagers are enabled on the server.
- **Added** | `updateChecker(Integer recourceID, ChatColor borderColor, ChatColor textColor, String character, String permission, Double frequencyHours)` | A fancy update checker!
- **Added** | `hex(String msg)` | Translates all the #HEX codes in the message to ChatColor codes.