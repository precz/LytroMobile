package pl.lytromobile.lytro

import java.net.InetSocketAddress
import java.net.Socket

/**
 * Minimalny transport TCP. Wątek wywołujący musi być wątkiem IO.
 * Lytro pierwszej generacji: typowo 10.100.1.1:5678 (sterowanie)
 * i 10.100.1.1:5679 (transfer danych).
 */
class LytroConnection(
    private val connectTimeoutMs: Int = 4_000,
    private val readTimeoutMs: Int = 8_000
) {
    fun probe(host: String, port: Int): Boolean {
        Socket().use { socket ->
            socket.connect(InetSocketAddress(host, port), connectTimeoutMs)
            socket.soTimeout = readTimeoutMs
            return socket.isConnected
        }
    }
}
