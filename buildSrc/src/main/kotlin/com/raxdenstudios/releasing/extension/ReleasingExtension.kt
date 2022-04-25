package com.raxdenstudios.releasing.extension

import com.raxdenstudios.releasing.model.GitCredentials

open class ReleasingExtension {
  var versionFilePath: String = "./version.properties"
  var credentials: GitCredentials = GitCredentials("", "")
}
