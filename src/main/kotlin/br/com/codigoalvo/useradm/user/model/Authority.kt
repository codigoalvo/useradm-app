package br.com.codigoalvo.useradm.user.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import java.util.*

@Entity
@Table(name = "authority")
class Authority(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val name: String = ""
) : GrantedAuthority {

    override fun getAuthority(): String {
        return name
    }

    constructor() : this(null, "")
}
