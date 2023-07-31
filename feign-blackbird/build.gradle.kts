plugins {
	`java-library`
}

projectConfiguration {
	artifactId.set("feign-blackbird")
	artifactDisplayName.set("Feign - Jackson Blackbird Extension")
	artifactDescription.set("Feign - Jackson Blackbird Extension")
}

dependencies {
	// modules
	api(project(":feign-common"))

	// annotations
	implementation("org.jetbrains:annotations:24.0.1")

	// feign
	api("io.github.openfeign:feign-core")

	// blackbird
	api("com.fasterxml.jackson.module:jackson-module-blackbird")
}
