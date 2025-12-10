plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    kotlin("android") version "1.9.10" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.5" apply false
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
