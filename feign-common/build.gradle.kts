plugins {
    `java-library`
}

projectConfiguration {
    artifactId.set("feign-common")
    artifactDisplayName.set("Feign Common")
    artifactDescription.set("Feign Common")
}

dependencies {
    // annotations
    implementation("org.jetbrains:annotations:24.0.1")

    // feign
    api("io.github.openfeign:feign-core")
}
