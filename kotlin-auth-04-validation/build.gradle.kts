import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String = "1.2.3"
val kotlinVersion: String = "1.3.50"
val logbackVersion: String = "1.2.3"
val jUnitVersion = "5.4.2"

val koinVersion: String = "2.0.1"
val hikariCpVersion: String = "3.3.1"
val h2DatabaseVersion: String = "1.4.199"
val flywayVersion: String = "5.2.4"

val ebeanVersion: String = "11.43.3"
val ebeanQueryBeanVersion: String = "11.43.2"
val ebeanQueryGeneratorVersion: String = "11.43.3"
val jaxbVersion: String = "2.3.1"

val hibernateValidatorVersion: String = "6.0.15.Final"
val javaxElVersion: String = "3.0.1-b11"
val javaxElApiVersion: String = "3.0.1-b06"

plugins {
    kotlin("jvm") version "1.3.50"
    kotlin("kapt") version "1.3.50"
    id("com.github.johnrengelman.shadow").version("5.1.0")
    id("io.ebean").version("11.39.1")

    application
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-gson:$ktorVersion")

    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-ktor:$koinVersion")
    implementation("org.koin:koin-logger-slf4j:$koinVersion")

    runtime("com.h2database:h2:$h2DatabaseVersion")
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    implementation("com.zaxxer:HikariCP:$hikariCpVersion")

    implementation("io.ebean:ebean:$ebeanVersion")
    implementation("io.ebean:ebean-querybean:$ebeanQueryBeanVersion")
    kapt("io.ebean:kotlin-querybean-generator:$ebeanQueryGeneratorVersion")
    implementation("javax.xml.bind:jaxb-api:$jaxbVersion")
    implementation("org.glassfish.jaxb:jaxb-runtime:$jaxbVersion")

    implementation("org.hibernate.validator:hibernate-validator:$hibernateValidatorVersion")
    implementation("org.hibernate.validator:hibernate-validator-annotation-processor:$hibernateValidatorVersion")
    implementation("org.glassfish:javax.el:$javaxElVersion")
    implementation("javax.el:javax.el-api:$javaxElApiVersion")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

val ktorEngineName: String = "io.ktor.server.netty.EngineMain"

application {
    mainClassName = ktorEngineName
}

tasks.shadowJar {
    manifest {
        attributes("Main-Class" to ktorEngineName)
    }
}

tasks.withType<KotlinCompile> {

    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"

    kotlinOptions {
        jvmTarget = "1.8"
        apiVersion = "1.3"
        languageVersion = "1.3"
        allWarningsAsErrors = true
    }
}

tasks.test {

    useJUnitPlatform {
        includeEngines("junit-jupiter", "spek2")
    }

    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events("passed", "failed", "skipped")
    }
}

@Suppress("UNCHECKED_CAST")
task<JavaExec>("generateMigration") {
    group = "migration"
    main = "id.jasoet.auth.migration.GenerateKt"
    classpath = sourceSets["main"].runtimeClasspath
    systemProperties = System.getProperties().toMap() as Map<String, Any?>
}

@Suppress("UNCHECKED_CAST")
task<JavaExec>("executeMigration") {
    group = "migration"
    main = "id.jasoet.auth.migration.ExecuteKt"
    classpath = sourceSets["main"].runtimeClasspath
    systemProperties = System.getProperties().toMap() as Map<String, Any?>
}

tasks.wrapper {
    gradleVersion = "5.6.1"
}