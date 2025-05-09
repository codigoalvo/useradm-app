package br.com.codigoalvo.useradm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class UserAdmApplication

fun main(args: Array<String>) {
    runApplication<UserAdmApplication>(*args)
}
