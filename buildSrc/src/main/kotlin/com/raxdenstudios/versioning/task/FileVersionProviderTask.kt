package com.raxdenstudios.versioning.task

import com.raxdenstudios.versioning.extension.VersioningExtension
import com.raxdenstudios.versioning.provider.FileVersionProvider
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input

open class FileVersionProviderTask : DefaultTask() {

  companion object {
    const val EXTENSION_NAME = "versioning"
  }

  private val extension: VersioningExtension by lazy {
    project.extensions.getByName(EXTENSION_NAME) as VersioningExtension
  }
  private val fileVersionProvider: FileVersionProvider by lazy {
    FileVersionProvider(extension.versionFilePath)
  }

  @get:Input
  val major: Int
    get() = fileVersionProvider.major

  @get:Input
  val minor: Int
    get() = fileVersionProvider.minor

  @get:Input
  val patch: Int
    get() = fileVersionProvider.patch

  @get:Input
  val dev: Int
    get() = fileVersionProvider.dev
  @get:Input
  val versionCode: Int
    get() = fileVersionProvider.versionCode
  @get:Input
  val versionName: String
    get() = fileVersionProvider.versionName
}
