plugins {
    `java-library`
}

projectConfiguration {
    artifactId.set("common")
    artifactDisplayName.set("common")
    artifactDescription.set("common")
}

dependencies {
    // annotations
    implementation("org.jetbrains:annotations:24.0.1")
}
