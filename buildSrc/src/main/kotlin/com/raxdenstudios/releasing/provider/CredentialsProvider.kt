package com.raxdenstudios.releasing.provider

import com.raxdenstudios.releasing.model.GitCredentials

class CredentialsProvider(
  private val credentials: GitCredentials
) {

  val user: String
    get() = credentials.user
  val password: String
    get() = credentials.password
}
