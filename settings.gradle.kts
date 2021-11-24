rootProject.name = "Minecraft-Window-Title"

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven(url = "https://maven.fabricmc.net/") { name = "Fabric" }
		maven(url = "https://repo.spongepowered.org/repository/maven-public/") { name = "Sponge Snapshots" }
	}
}

if (settings.extra.has("forgeVersion")) {
	include("Forge")
}

if (settings.extra.has("fabricVersion")) {
	include("Fabric")
}
