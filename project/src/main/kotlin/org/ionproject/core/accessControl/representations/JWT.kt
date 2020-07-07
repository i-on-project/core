package org.ionproject.core.accessControl.representations

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

interface JWT

class JWTHeaderRepr (
  val typ : String,
  val alg : String
) : JWT

class JWTPayloadRepr (
  val client_id : Int,
  val url : String,
  val iat : Long,
  val exp : Long
) : JWT

fun serializeComponent(JWTComponent: JWT): String {
  val mapper = jacksonObjectMapper()
  return mapper.writeValueAsString(JWTComponent)
}