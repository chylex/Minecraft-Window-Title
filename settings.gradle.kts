rootProject.name = "Minecraft-Window-Title"

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven(url = "https://maven.neoforged.net/releases") { name = "NeoForge" }
		maven(url = "https://maven.fabricmc.net/") { name = "Fabric" }
		maven(url = "https://repo.spongepowered.org/repository/maven-public/") { name = "Sponge Snapshots" }
	}
	
	plugins {
		if (settings.extra.has("neoForgeVersion")) {
			id("net.neoforged.gradle.vanilla") version "7.0.41"
			id("net.neoforged.gradle.userdev") version "7.0.41"
			id("net.neoforged.gradle.mixin") version "7.0.41"
		}
		
		if (settings.extra.has("loomVersion")) {
			id("fabric-loom") version "${settings.extra["loomVersion"]}-SNAPSHOT"
		}
	}
}

if (settings.extra.has("neoForgeVersion")) {
	include("NeoForge")
}

if (settings.extra.has("fabricVersion")) {
	include("Fabric")
}
