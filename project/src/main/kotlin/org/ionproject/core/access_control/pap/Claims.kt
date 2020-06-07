package org.ionproject.core.access_control.pap

class Claims(
  val iat: Long,
  val exp: Long,
  val client_id: Int,
  val scope: String
)