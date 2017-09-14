package kotlinworkshop

import com.vaadin.annotations.Push
import com.vaadin.server.VaadinRequest
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Grid
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.TextField
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout


@SpringUI(path = "")
@Push
class UserUI(val dao: UserDAO): UI() {

    val searchAllButton = Button("Get All Users")
    val idSearchField = TextField("ID")
    val table = Grid<Any>().apply { //change this to Grid<User>(User::class.java) after you created the User class
        setSizeFull()
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
                println("searchAllButton")
            }
            idSearchField.addValueChangeListener { e ->
                println("idSearchField: ${e.value}")
            }
            generateUsersButton.addClickListener {
                println("generateUsersButton: ${generateUsersAmount.value}")
            }
            activeEmailsButton.addClickListener {
                println("showMailsOfActiveUsers")
            }
        }
    }

}
