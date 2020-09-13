import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import model.Message
import org.w3c.dom.EventSource
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.fetch.RequestInit
import kotlin.js.json

@ExperimentalCoroutinesApi
suspend fun main() {
    val userSelect = document.getElementById("user-select") as HTMLSelectElement
    val messageInput = document.getElementById("message-input") as HTMLInputElement
    val submitButton = document.getElementById("submit-button") as HTMLInputElement

    console.log("FRONTEND WORKS")

    messageInput.onkeypress =  {
        if (it.keyCode == 13 && messageInput.checkValidity()) {
            submit(Message(userSelect.value, messageInput.value))
            messageInput.value = ""
            it.preventDefault()
        }
    }

    submitButton.onclick =  {
        if (messageInput.checkValidity()) {
            submit(Message(userSelect.value, messageInput.value))
            messageInput.value = ""
        }
    }

    EventSource("/message/stream").asFlow()
            .map { JSON.parse<Message>(it) }
            .collect {
                val li = document.createElement("li").apply {
                    innerHTML = "<b>${it.user}</b> ${it.content}"
                }
                document.getElementById("messages")!!.appendChild(li)
            }

}

fun submit(message: Message) {
    window.fetch("/message/send", init = RequestInit(
            method = "POST",
            headers = json("Content-Type" to "application/json"),
            body = JSON.stringify(message)
    ))
}
