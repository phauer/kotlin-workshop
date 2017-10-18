package kotlinworkshop

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UserDAO(private val template: JdbcTemplate) {

    fun findAllUsers() = template.query("SELECT * FROM users;", this::mapToUser)

    private fun mapToUser(rs: ResultSet, rowNum: Int) = User(
            id = rs.getInt("id")
            , email = rs.getString("email")
            , description = rs.getString("description")
            , name = mergeNames(rs)
            , role = if (rs.getBoolean("guest")) Role.GUEST else Role.USER
            , platform = rs.getString("platform").mapToPlatform()
            , dateCreated = rs.getTimestamp("date_created").toInstant()
            , state = State.valueOf(rs.getString("state"))
    )

    fun findUser(id: Int) = try {
        template.queryForObject("SELECT * FROM users WHERE id = ?;", arrayOf(id), this::mapToUser)
    } catch (e: EmptyResultDataAccessException) {
        null
    }

    fun deleteUser(id: Int) = template.update("DELETE FROM users WHERE id = ?;", id)

    fun addUser(user: User) {
        template.update(
                "INSERT INTO users (id, email, firstname, lastname, description, guest, platform, date_created, state) VALUES (?, ?, ?, ?, ?, ?, ?, FROM_UNIXTIME(?), ?);",
                user.id, user.email, user.name, null,
                user.description,
                user.role == Role.GUEST,
                user.platform.toString(),
                user.dateCreated.epochSecond,
                user.state.toString()
        )
    }
}

private fun String?.mapToPlatform() = when(this) {
    "eu", "EU", "europe" -> Platform.EU
    "na", "NA", "northamerica" -> Platform.NA
    null -> null
    else -> throw UserDAOException("Unknown platform $this")
}

private fun mergeNames(rs: ResultSet): String? {
    val firstname = rs.getString("firstname") ?: ""
    val lastname = rs.getString("lastname") ?: ""
    val name = "$firstname $lastname"
    return if (name.isBlank()) null else name
}

class UserDAOException(message: String): RuntimeException(message)
