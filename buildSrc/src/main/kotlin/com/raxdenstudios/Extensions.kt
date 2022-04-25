package com.raxdenstudios

import org.ajoberstar.grgit.Grgit
import java.util.*

fun Grgit.checkoutBranch(branchName: String) {
  if (branch.list().any { branch -> branch.name == branchName }) {
    checkout { branch = branchName }
    pull()
  } else {
    checkout {
      branch = branchName
      startPoint = "origin/$branchName"
      createBranch = true
    }
  }
}

fun Properties.getPropertyOrDefault(key: String, default: String): String =
  getProperty(key) ?: default
