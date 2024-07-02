import org.gradle.jvm.tasks.Jar

val modId: String by project
val minecraftVersion: String by project
val fabricVersion: String by project

plugins {
	id("fabric-loom")
}

dependencies {
	minecraft("com.mojang:minecraft:$minecraftVersion")
	modImplementation("net.fabricmc:fabric-loader:$fabricVersion")
	mappings(loom.officialMojangMappings())
}

loom {
	runs {
		val runJvmArgs: Set<String> by project
		
		configureEach {
			runDir("../run")
			vmArgs(runJvmArgs)
			ideConfigGenerated(true)
		}
		
		named("client") {
			configName = "Fabric Client"
			client()
		}
		
		findByName("server")?.let(::remove)
	}
	
	mixin {
		defaultRefmapName.set("$modId.refmap.json")
	}
}

tasks.processResources {
	filesMatching("fabric.mod.json") {
		expand(inputs.properties)
	}
}

tasks.register<Jar>("uncompressedRemapJar") {
	group = "fabric"
	
	from(tasks.remapJar.map { it.outputs.files.map(::zipTree) })

	archiveClassifier.set("uncompressed")
	entryCompression = ZipEntryCompression.STORED // Reduces size of multiloader jar.
}
