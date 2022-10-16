@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    java
    application
    alias(libs.plugins.jagr)
}

jagr {
    assignmentId.set("h_id_")
    submissions {
        val main by creating {
// ACHTUNG! Entfernen Sie '//' in den folgenden Zeilen und setzen Sie Ihre TU-ID (NICHT Matrikelnummer!), Vor- und Nachnamen ein.
//            studentId.set("ab12cdef")
//            firstName.set("FirstName")
//            lastName.set("LastName")
        }
    }
    graders {
        val graderPublic by creating {
            graderName.set("_name_-Public")
            rubricProviderName.set("h_id_.H_id__RubricProvider")
        }
    }
}

dependencies {
    implementation(libs.algoutils.student)
    implementation(libs.annotations)
    // JUnit only available in "test" source set (./src/test)
    testImplementation(libs.junit)
}

application {
    mainClass.set("h_id_.Main")
}

tasks {
    val runDir = File("build/run")
    named<JavaExec>("run") {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
    }
    test {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
        useJUnitPlatform()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}
