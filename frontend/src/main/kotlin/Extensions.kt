import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import model.Message
import org.w3c.dom.EventSource

@ExperimentalCoroutinesApi
fun EventSource.asFlow() = callbackFlow<String> {
    onmessage = {
        offer(it.data as String)
    }
    onerror = {
        cancel(CancellationException("Event source failed"))
    }
    awaitClose {
        this@asFlow.close()
    }
}
