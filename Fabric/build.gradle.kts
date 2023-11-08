val modId: String by project
val minecraftVersion: String by project
val fabricVersion: String by project

plugins {
	id("fabric-loom")
}

repositories {
	maven("https://repo.spongepowered.org/maven")
}

dependencies {
	minecraft("com.mojang:minecraft:$minecraftVersion")
	modImplementation("net.fabricmc:fabric-loader:$fabricVersion")
	mappings(loom.officialMojangMappings())
}

loom {
	runs {
		configureEach {
			runDir("../run")
			ideConfigGenerated(true)
		}
		
		named("client") {
			configName = "Fabric Client"
			client()
		}
		
		findByName("server")?.let(::remove)
	}
	
	mixin {
		add(sourceSets.main.get(), "$modId.refmap.json")
	}
}

tasks.processResources {
	filesMatching("fabric.mod.json") {
		expand(inputs.properties)
	}
}

tasks.remapJar {
	archiveVersion.set(tasks.jar.get().archiveVersion)
}
