val modId: String by project
val minecraftVersion: String by project
val neoForgeVersion: String by project
val mixinVersion: String by project

plugins {
	id("net.neoforged.gradle.userdev")
	id("net.neoforged.gradle.mixin")
}

dependencies {
	implementation("net.neoforged:neoforge:$neoForgeVersion")
}

runs {
	configureEach {
		modSource(project.sourceSets.main.get())
		workingDirectory = file("../run")
	}
	
	create("client")
}

tasks.processResources {
	filesMatching("META-INF/mods.toml") {
		expand(inputs.properties)
	}
}
