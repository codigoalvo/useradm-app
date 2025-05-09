package br.com.codigoalvo.useradm.user.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "\"user\"")
class User() : UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    lateinit var email: String

    @Column(nullable = false)
    lateinit var name: String

    var avatar: String? = null

    @Column(name = "password", nullable = false)
    private lateinit var _password: String

    var expirationDate: LocalDateTime? = null
    var locked: Boolean = false
    var enabled: Boolean = true
    var createdDate: LocalDateTime = LocalDateTime.now()
    var modifiedDate: LocalDateTime? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    var roles: Set<Role> = mutableSetOf()

    constructor(
        email: String,
        name: String,
        avatar: String? = null,
        password: String,
        expirationDate: LocalDateTime? = null,
        locked: Boolean = false,
        enabled: Boolean = true,
        createdDate: LocalDateTime = LocalDateTime.now(),
        modifiedDate: LocalDateTime? = null,
        roles: Set<Role> = mutableSetOf()
    ) : this() {
        this.email = email
        this.name = name
        this.avatar = avatar
        this._password = password
        this.expirationDate = expirationDate
        this.locked = locked
        this.enabled = enabled
        this.createdDate = createdDate
        this.modifiedDate = modifiedDate
        this.roles = roles
    }

    override fun getPassword(): String = _password
    override fun getUsername(): String = email
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return roles.flatMap { role ->
            role.authorities.map { authority -> SimpleGrantedAuthority(authority.name) }
        }
    }

    override fun isAccountNonExpired(): Boolean =
        expirationDate?.isAfter(LocalDateTime.now()) ?: true

    override fun isAccountNonLocked(): Boolean = !locked
    override fun isCredentialsNonExpired(): Boolean =
        expirationDate?.isAfter(LocalDateTime.now()) ?: true

    override fun isEnabled(): Boolean = enabled
}
