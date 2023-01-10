package me.casiebarie.keeslib;

/**
 * An enum with all the bukkitversions with {@code IsLegacy} to show if it is a legacy version.
 * @version 1.0.1
 * @since 1.0.0
 */
public enum BukkitVersions {
	v1_8_R1(true),
	v1_8_R2(true),
	v1_8_R3(true),
	v1_9_R1(true),
	v1_9_R2(true),
	v1_10_R1(true),
	v1_11_R1(true),
	v1_12_R1(true),
	v1_13_R1(false),
	v1_13_R2(false),
	v1_14_R1(false),
	v1_15_R1(false),
	v1_16_R1(false),
	v1_16_R2(false),
	v1_16_R3(false),
	v1_17_R1(false),
	v1_18_R1(false),
	v1_18_R2(false),
	v1_19_R1(false),
	v1_19_R2(false),
	v1_20_R1(false);

	final Boolean isLegacy;
	BukkitVersions(Boolean isLegacy) {this.isLegacy = isLegacy;}
}
