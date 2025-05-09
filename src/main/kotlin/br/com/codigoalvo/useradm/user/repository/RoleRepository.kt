package br.com.codigoalvo.useradm.user.repository

import br.com.codigoalvo.useradm.user.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role, UUID>
