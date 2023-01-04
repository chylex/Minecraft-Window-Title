val modId: String by project
val minecraftVersion: String by project
val fabricVersion: String by project

plugins {
	idea
	id("fabric-loom")
}

dependencies {
	minecraft("com.mojang:minecraft:$minecraftVersion")
	modImplementation("net.fabricmc:fabric-loader:$fabricVersion")
	mappings(loom.officialMojangMappings())
}

loom {
	runs {
		named("client") {
			configName = "Fabric Client"
			client()
			runDir("../run")
			ideConfigGenerated(true)
		}
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
