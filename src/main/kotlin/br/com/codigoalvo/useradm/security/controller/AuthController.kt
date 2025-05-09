package br.com.codigoalvo.useradm.security.controller

import br.com.codigoalvo.useradm.security.dto.LoginRequest
import br.com.codigoalvo.useradm.security.dto.LoginResponse
import br.com.codigoalvo.useradm.security.jwt.TokenService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val tokenService: TokenService
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val authToken = UsernamePasswordAuthenticationToken(request.email, request.password)
        val authentication = authenticationManager.authenticate(authToken)
        val token = tokenService.generateToken(authentication)
        return ResponseEntity.ok(LoginResponse(token))
    }
}
