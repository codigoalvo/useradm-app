package br.com.codigoalvo.useradm.user.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "role")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val name: String = "",

    @ManyToMany
    @JoinTable(
        name = "role_authority",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "authority_id")]
    )
    val authorities: Set<Authority> = emptySet()
) {
    constructor() : this(null, "", emptySet())
}
