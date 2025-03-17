// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
     val agp_version by extra("8.4.0")
  //  agp_version = "8.4.0"

}

plugins {
    id("com.android.application") version "8.4.0" apply false
    id("com.android.library") version "8.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22" apply false
}