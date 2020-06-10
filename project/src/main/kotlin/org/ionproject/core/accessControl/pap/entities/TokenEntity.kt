package org.ionproject.core.accessControl.pap.entities

import org.ionproject.core.accessControl.pap.entities.ClaimsEntity

class TokenEntity(
  val hash: String,
  val isValid: Boolean,
  val issuedAt: Long,
  val expiresAt: Long,
  val claims: ClaimsEntity
)