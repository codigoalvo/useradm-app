package br.com.codigoalvo.useradm.security.dto

data class LoginResponse(
    val token: String,
    val type: String = "Bearer"
)
