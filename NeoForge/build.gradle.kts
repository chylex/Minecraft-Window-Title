val neoForgeVersion: String by project

plugins {
	id("net.neoforged.gradle.userdev")
	id("net.neoforged.gradle.mixin")
}

dependencies {
	implementation("net.neoforged:neoforge:$neoForgeVersion")
}

runs {
	val runJvmArgs: Set<String> by project
	
	configureEach {
		workingDirectory = file("../run")
		modSource(project.sourceSets.main.get())
		jvmArguments(runJvmArgs)
	}
	
	removeIf { it.name != "client" }
}

tasks.processResources {
	filesMatching("META-INF/neoforge.mods.toml") {
		expand(inputs.properties)
	}
}
