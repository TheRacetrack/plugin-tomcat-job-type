
repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly("org.apache.tomcat:tomcat-servlet-api:10.1.15")
    implementation("org.apache.tomcat:tomcat-catalina:10.1.15")

    implementation("io.prometheus:prometheus-metrics-core:1.0.0")
    implementation("io.prometheus:prometheus-metrics-model:1.0.0")
    implementation("io.prometheus:prometheus-metrics-config:1.0.0")
    implementation("io.prometheus:prometheus-metrics-instrumentation-jvm:1.0.0")
    implementation("io.prometheus:prometheus-metrics-exporter-servlet-jakarta:1.0.0")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    // Define the main class for the application.
    mainClass.set("Prometheus")
}

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    // Needed to create a war file for Tomcat.
    war
}

tasks.war {
    // Specify contents to be included in the WAR file
    from("src") {
        include("WEB-INF/web.xml")
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
