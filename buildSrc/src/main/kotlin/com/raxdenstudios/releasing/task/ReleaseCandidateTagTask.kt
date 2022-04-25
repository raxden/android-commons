package com.raxdenstudios.releasing.task

import com.raxdenstudios.checkoutBranch
import org.ajoberstar.grgit.Grgit
import org.gradle.api.tasks.TaskAction

open class ReleaseCandidateTagTask : AbstractReleaseCandidateTask() {

  @TaskAction
  fun execute() {
    println("==== Release candidate task has started ====")
    openGitWithCredentials().run {
      checkoutBranch(releaseBranch)
      createTagRelease()
      bumpVersion()
      checkoutBranch(MASTER_BRANCH)
      close()
    }
  }

  private fun Grgit.createTagRelease() {
    tag.add { name = versionName }
    push { tags = true }
  }

  private fun Grgit.bumpVersion() {
    increasePatchVersion()
    add { patterns = mutableSetOf(".") }
    commit { message = commitMessage }
    push()
  }
}
