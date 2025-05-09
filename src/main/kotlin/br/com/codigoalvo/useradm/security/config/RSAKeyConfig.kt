package br.com.codigoalvo.useradm.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Configuration
open class RSAKeyConfig {

    @Bean
    open fun publicKey(): RSAPublicKey {
        val resource = ClassPathResource("certs/public_key.pem")
        val keyBytes = resource.inputStream.use { it.readBytes() }

        println("Bytes da chave pública: ${keyBytes.size} bytes")

        try {
            val keyData = String(keyBytes).replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replace("\n", "").replace("\r", "")

            val decodedKey = Base64.getDecoder().decode(keyData)

            val keySpec = X509EncodedKeySpec(decodedKey)
            val keyFactory = KeyFactory.getInstance("RSA")
            return keyFactory.generatePublic(keySpec) as RSAPublicKey
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Erro ao processar chave pública", e)
        }
    }

    @Bean
    open fun privateKey(): RSAPrivateKey {
        val resource = ClassPathResource("certs/private_key.pem")
        val keyBytes = resource.inputStream.use { it.readBytes() }


        println("Bytes da chave privada: ${keyBytes.size} bytes")

        try {
            val keyData = String(keyBytes).replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replace("\n", "").replace("\r", "")

            val decodedKey = Base64.getDecoder().decode(keyData)

            val keySpec = PKCS8EncodedKeySpec(decodedKey)
            val keyFactory = KeyFactory.getInstance("RSA")
            return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Erro ao processar chave privada", e)
        }

    }
}

