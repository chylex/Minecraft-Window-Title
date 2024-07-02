rootProject.name = "Minecraft-Window-Title"

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven(url = "https://maven.neoforged.net/releases") { name = "NeoForge" }
		maven(url = "https://maven.fabricmc.net/") { name = "Fabric" }
	}
	
	plugins {
		val neoGradleVersion = settings.extra.get("neoGradleVersion") as? String
		if (neoGradleVersion != null) {
			id("net.neoforged.gradle.userdev") version neoGradleVersion
			id("net.neoforged.gradle.mixin") version neoGradleVersion
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
