import net.fabricmc.loom.configuration.ide.RunConfigSettings
import org.gradle.jvm.tasks.Jar

val modId: String by project
val modSides: String by project
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
		
		fun side(name: String, configure: RunConfigSettings.() -> Unit) {
			if (modSides == "both" || modSides == name) {
				named(name, configure)
			}
			else {
				findByName(name)?.let(::remove)
			}
		}
		
		side("client") {
			configName = "Fabric Client"
			client()
		}
		
		side("server") {
			configName = "Fabric Server"
			server()
		}
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
