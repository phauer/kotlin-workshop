package kotlinworkshop

import com.vaadin.annotations.Push
import com.vaadin.icons.VaadinIcons
import com.vaadin.server.VaadinRequest
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Grid
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Notification
import com.vaadin.ui.TextField
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import java.time.Instant
import java.util.Random


@SpringUI(path = "")
@Push
class UserUI(val dao: UserDAO): UI() {

    val searchAllButton = Button("Get All Users")
    val idSearchField = TextField("ID")
    val table = Grid<User>(User::class.java).apply {
        setSizeFull()
        addComponentColumn { person ->
            Button(VaadinIcons.TRASH).apply {
                addStyleName(ValoTheme.BUTTON_BORDERLESS)
                addClickListener{ deleteUser(person.id) }
            }
        }
    }
    val activeEmailsButton = Button("Show Mails of Active Users")
    val generateUsersAmount = TextField("Users Amount")
    val generateUsersButton = Button("Generate Users")

    override fun init(request: VaadinRequest) {
        page.setTitle("Kotlin Workshop")
        content = VerticalLayout().apply {
            val generateForm = HorizontalLayout(generateUsersAmount, generateUsersButton).apply {
                setComponentAlignment(generateUsersButton, Alignment.BOTTOM_CENTER)
            }
            addComponents(searchAllButton, idSearchField, table, activeEmailsButton, generateForm)
            setExpandRatio(table, 1f)

            searchAllButton.addClickListener {
                refresh()
            }
            idSearchField.addValueChangeListener { e ->
                searchUserById(e.value)
            }
            generateUsersButton.addClickListener {
                generateUsers(generateUsersAmount.value)
                refresh()
            }
            activeEmailsButton.addClickListener {
                showMailsOfActiveUsers()
            }
        }
    }

    private fun showMailsOfActiveUsers() {
        val emails = dao.findAllUsers().filter { it.state == State.ACTIVATED }.map { it.email }
        Notification.show("The Mails: $emails")
    }

    private fun searchUserById(idInput: String) {
        val id = idInput.toIntOrNull() ?: return
        val user = dao.findUser(id)
        if (user != null) {
            table.setItems(user)
        } else {
            table.setItems(listOf())
        }
    }

    private fun refresh() {
        val users = dao.findAllUsers()
        table.setItems(users)
    }

    private fun generateUsers(amountString: String) {
        val amount = amountString.toIntOrNull() ?: 0
        if (amount != 0) {
            val dummyUsers = (1..amount).map { createDummyUser() }
            dao.addUsers(dummyUsers)
        }
    }

    private fun deleteUser(userId: Int){
        val affectedRows = dao.deleteUser(userId)
        Notification.show("Deleted rows: $affectedRows")
        table.setItems(dao.findAllUsers())
    }
}

private fun createDummyUser() = User(
        id = Random().nextInt(99999)
        , email = "dummy@gmail.com"
        , description = "nice dummy"
        , name = "Dummy"
        , role = Role.USER
        , platform = Platform.EU
        , dateCreated = Instant.now()
        , state = State.ACTIVATED
)
