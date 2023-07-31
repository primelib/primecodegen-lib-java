plugins {
	`java-library`
}

projectConfiguration {
	artifactId.set("feign-resilience4j")
	artifactDisplayName.set("Feign Resilience4J Capability")
	artifactDescription.set("Feign Resilience4J Capability")
}

dependencies {
	// modules
	api(project(":feign-common"))

	// annotations
	implementation("org.jetbrains:annotations:24.0.1")

	// feign
	api("io.github.openfeign:feign-core")

	// resilience4J
	api("io.github.resilience4j:resilience4j-core")
	api("io.github.resilience4j:resilience4j-retry")
	api("io.github.resilience4j:resilience4j-bulkhead")
	api("io.github.resilience4j:resilience4j-ratelimiter")
	api("io.github.resilience4j:resilience4j-circuitbreaker")
	api("io.github.resilience4j:resilience4j-micrometer")
}
