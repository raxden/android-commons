package com.raxdenstudios.releasing.task

import com.raxdenstudios.checkoutBranch
import org.ajoberstar.grgit.Grgit
import org.gradle.api.tasks.TaskAction

open class ReleaseCandidateBranchTask : AbstractReleaseCandidateTask() {

  @TaskAction
  fun execute() {
    openGitWithCredentials().run {
      checkoutBranch(MASTER_BRANCH)
      bumpVersion()
      createReleaseCandidateBranch()
      close()
    }
  }

  private fun Grgit.createReleaseCandidateBranch() = push {
    remote = "origin"
    refsOrSpecs = listOf("HEAD:refs/heads/$releaseBranch")
  }

  private fun Grgit.bumpVersion() {
    increaseMinorVersion()
    resetPatchVersion()
    add { patterns = mutableSetOf(".") }
    commit { message = commitMessage }
    push()
  }
}
