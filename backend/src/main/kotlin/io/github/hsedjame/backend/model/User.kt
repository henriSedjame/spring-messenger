package io.github.hsedjame.backend.model

import model.User
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class User(@Id val id: Long?) : User(null, null, null) {}
