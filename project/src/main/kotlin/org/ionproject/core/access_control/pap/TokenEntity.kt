package org.ionproject.core.access_control.pap

class TokenEntity(
  val hash: String,
  val isValid: Boolean,
  val claims: Claims
)