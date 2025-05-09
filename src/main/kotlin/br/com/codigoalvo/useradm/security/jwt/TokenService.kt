package br.com.codigoalvo.useradm.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import java.io.InputStreamReader
import java.security.KeyFactory
import java.security.Signature
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import java.time.Instant
import br.com.codigoalvo.useradm.user.model.User
import br.com.codigoalvo.useradm.user.model.Role
import kotlin.text.Charsets.UTF_8

@Service
class TokenService(
    @Value("\${security.jwt.expiration}") private val expiration: Long,
    @Value("\${security.jwt.issuer}") private val issuer: String
) {
    private val privateKey: RSAPrivateKey by lazy {
        val resource = ClassPathResource("certs/private_key.pem")
        val keyBytes = InputStreamReader(resource.inputStream).readText()

        val cleanedKey = keyBytes
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\n", "")
            .replace("\r", "")

        val decodedKey = Base64.getDecoder().decode(cleanedKey)

        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = PKCS8EncodedKeySpec(decodedKey)

        keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }

    // Método para gerar o token JWT
    fun generateToken(authentication: Authentication): String {
        val principal = authentication.principal as User

        val now = Instant.now()
        val expiry = now.plusSeconds(expiration)

        val claims = mapOf(
            "sub" to principal.username,
            "roles" to extractRoles(principal.roles),
            "authorities" to extractAuthorities(principal.authorities),
            "iss" to issuer,
            "iat" to now.epochSecond,
            "exp" to expiry.epochSecond
        )

        val header = mapOf("alg" to "RS256", "typ" to "JWT")

        val mapper = ObjectMapper()
        val encodedHeader = Base64.getUrlEncoder().withoutPadding()
            .encodeToString(mapper.writeValueAsBytes(header))
        val encodedPayload = Base64.getUrlEncoder().withoutPadding()
            .encodeToString(mapper.writeValueAsBytes(claims))

        val contentToSign = "$encodedHeader.$encodedPayload"
        val signature = signWithRsa(contentToSign.toByteArray(UTF_8))
        val encodedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signature)

        return "$encodedHeader.$encodedPayload.$encodedSignature"
    }

    private fun extractRoles(roles: Set<Role>): Set<String> {
        return roles.map { it.name }.toSet()
    }

    private fun extractAuthorities(authorities: Collection<GrantedAuthority>): Set<String> {
        return authorities.map { it.authority }.toSet()
    }

    private fun signWithRsa(data: ByteArray): ByteArray {
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initSign(privateKey)
        signature.update(data)
        return signature.sign()
    }
}
