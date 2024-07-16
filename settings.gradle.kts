rootProject.name = "Minecraft-Window-Title"

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven(url = "https://maven.neoforged.net/releases") { name = "NeoForge" }
		maven(url = "https://maven.fabricmc.net/") { name = "Fabric" }
	}
	
	plugins {
		val neoModDevVersion = settings.extra.get("neoModDevVersion") as? String
		if (neoModDevVersion != null) {
			id("net.neoforged.moddev") version neoModDevVersion
		}
		
		val loomVersion = settings.extra.get("loomVersion") as? String
		if (loomVersion != null) {
			id("fabric-loom") version "$loomVersion-SNAPSHOT"
		}
	}
}

if (settings.extra.has("neoForgeVersion")) {
	include("NeoForge")
}

if (settings.extra.has("fabricVersion")) {
	include("Fabric")
}
