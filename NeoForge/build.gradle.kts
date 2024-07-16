val modId: String by project
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
		
		register("client") {
			ideName.set("NeoForge Client")
			client()
		}
	}
}

tasks.processResources {
	filesMatching("META-INF/neoforge.mods.toml") {
		expand(inputs.properties)
	}
}
