import net.neoforged.moddevgradle.dsl.RunModel

val modId: String by project
val modSides: String by project
val neoForgeVersion: String by project

plugins {
	id("net.neoforged.moddev")
}

neoForge {
	version = neoForgeVersion
	
	mods {
		register(modId) {
			sourceSet(sourceSets.main.get())
			sourceSet(rootProject.sourceSets.main.get())
		}
	}
	
	runs {
		val runJvmArgs: Set<String> by project
		
		configureEach {
			gameDirectory = file("../run")
			jvmArguments.addAll(runJvmArgs)
		}
		
		fun side(name: String, configure: RunModel.() -> Unit) {
			if (modSides == "both" || modSides == name) {
				register(name, configure)
			}
		}
		
		side("client") {
			ideName.set("NeoForge Client")
			client()
		}
		
		side("server") {
			ideName.set("NeoForge Server")
			server()
		}
	}
}

tasks.processResources {
	filesMatching("META-INF/neoforge.mods.toml") {
		expand(inputs.properties)
	}
}
