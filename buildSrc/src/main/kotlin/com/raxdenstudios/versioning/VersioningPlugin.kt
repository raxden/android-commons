package com.raxdenstudios.versioning

import com.android.build.gradle.BaseExtension
import com.raxdenstudios.versioning.extension.AppVersioningExtension
import com.raxdenstudios.versioning.extension.LibraryVersioningExtension
import com.raxdenstudios.versioning.extension.VersioningExtension
import com.raxdenstudios.versioning.provider.FileVersionProvider
import com.raxdenstudios.versioning.task.FileVersionProviderTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register

class VersioningPlugin : Plugin<Project> {

  companion object {
    private const val EXTENSION_NAME = "versioning"
    private const val GROUP_TASK_NAME = "versioning"
    private const val APPLICATION_PLUGIN = "com.android.application"
    private const val DYNAMIC_FEATURE_PLUGIN = "com.android.dynamic-feature"
    private const val LIBRARY_PLUGIN = "com.android.library"
    private const val FILE_VERSION_PROVIDER_TASK_NAME = "fileVersionProviderTask"
  }

  private lateinit var versioningExtension: VersioningExtension
  private lateinit var fileVersionProvider: FileVersionProvider

  override fun apply(project: Project) {
    project.initExtensionAccordingToPluginUsed()
    project.registerFileVersionProviderTask()
    project.configure()
  }

  private fun Project.initExtensionAccordingToPluginUsed() {
    pluginManager.withPlugin(APPLICATION_PLUGIN) { initAppVersioningExtension() }
    pluginManager.withPlugin(DYNAMIC_FEATURE_PLUGIN) { initAppVersioningExtension() }
    pluginManager.withPlugin(LIBRARY_PLUGIN) { initLibraryVersioningExtension() }
  }

  private fun Project.initLibraryVersioningExtension() {
    versioningExtension = extensions.create<LibraryVersioningExtension>(EXTENSION_NAME)
  }

  private fun Project.initAppVersioningExtension() {
    versioningExtension = extensions.create<AppVersioningExtension>(EXTENSION_NAME)
  }

  private fun Project.registerFileVersionProviderTask() {
    tasks.register<FileVersionProviderTask>(FILE_VERSION_PROVIDER_TASK_NAME) {
      group = GROUP_TASK_NAME
    }
  }

  private fun Project.configure() {
    afterEvaluate {
      initFileVersionProvider()
      extensions.getByType<BaseExtension>().run {
        pluginManager.withPlugin(APPLICATION_PLUGIN) { configureApplication() }
        pluginManager.withPlugin(DYNAMIC_FEATURE_PLUGIN) { configureApplication() }
        pluginManager.withPlugin(LIBRARY_PLUGIN) { configureLibrary() }
      }
    }
  }

  private fun initFileVersionProvider() {
    fileVersionProvider = FileVersionProvider(versioningExtension.versionFilePath)
  }

  private fun Project.configureLibrary() {
    version = fileVersionProvider.versionName
    group = (versioningExtension as LibraryVersioningExtension).group
  }

  private fun BaseExtension.configureApplication() {
    defaultConfig.versionName = fileVersionProvider.versionName
    defaultConfig.versionCode = fileVersionProvider.versionCode
  }
}
