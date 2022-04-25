package com.raxdenstudios.releasing

import com.raxdenstudios.releasing.extension.ReleasingExtension
import com.raxdenstudios.releasing.task.ReleaseCandidateBranchTask
import com.raxdenstudios.releasing.task.ReleaseCandidateTagTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class ReleasingPlugin : Plugin<Project> {

  companion object {
    private const val EXTENSION_NAME = "releasing"
    private const val GROUP_TASK_NAME = "releasing"
    private const val RELEASE_CANDIDATE_TAG_TASK_NAME = "releaseCandidateTag"
    private const val RELEASE_CANDIDATE_TASK_NAME = "releaseCandidate"
  }

  private lateinit var releasingExtension: ReleasingExtension

  override fun apply(project: Project) {
    project.initExtension()
    project.registerReleaseCandidateTask()
    project.registerReleaseCandidateTagTask()
  }

  private fun Project.initExtension() {
    releasingExtension = extensions.create(EXTENSION_NAME)
  }

  private fun Project.registerReleaseCandidateTagTask() {
    tasks.register<ReleaseCandidateTagTask>(RELEASE_CANDIDATE_TAG_TASK_NAME) {
      group = GROUP_TASK_NAME
    }
  }

  private fun Project.registerReleaseCandidateTask() {
    tasks.register<ReleaseCandidateBranchTask>(RELEASE_CANDIDATE_TASK_NAME) {
      group = GROUP_TASK_NAME
    }
  }
}
