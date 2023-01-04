rootProject.name = "Minecraft-Window-Title"

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven(url = "https://maven.fabricmc.net/") { name = "Fabric" }
		maven(url = "https://repo.spongepowered.org/repository/maven-public/") { name = "Sponge Snapshots" }
	}
	
	plugins {
		if (settings.extra.has("loomVersion")) {
			id("fabric-loom") version "${settings.extra["loomVersion"]}-SNAPSHOT"
		}
	}
}

if (settings.extra.has("forgeVersion")) {
	include("Forge")
}

if (settings.extra.has("fabricVersion")) {
	include("Fabric")
}
