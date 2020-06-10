package org.ionproject.core.accessControl.pap

class TokenEntity(
  val hash: String,
  val isValid: Boolean,
  val claims: Claims
)