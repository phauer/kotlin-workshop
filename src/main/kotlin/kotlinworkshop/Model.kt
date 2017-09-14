package kotlinworkshop

import java.time.Instant

data class User(
        val id: Int,
        val email: String,
        val name: String?,
        val description: String?,
        val role: Role,
        val platform: Platform?,
        val dateCreated: Instant,
        val state: State
)

enum class Platform { EU, NA }
enum class State { ACTIVATED, DEACTIVATED, DELETED }
enum class Role { USER, GUEST }