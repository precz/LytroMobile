package pl.lytromobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.lytromobile.lytro.LytroConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var status by remember { mutableStateOf("Gotowy. Włącz Wi-Fi w aparacie i połącz telefon z siecią Lytro.") }
                var busy by remember { mutableStateOf(false) }
                val scope = rememberCoroutineScope()

                Scaffold { padding ->
                    Column(
                        modifier = Modifier.padding(padding).padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Lytro Mobile", style = MaterialTheme.typography.headlineMedium)
                        Text("Prototyp połączenia Wi-Fi • wersja 0.1.0")
                        Text(status)
                        Button(
                            enabled = !busy,
                            onClick = {
                                busy = true
                                status = "Sprawdzam połączenie z aparatem…"
                                scope.launch {
                                    val result = withContext(Dispatchers.IO) {
                                        runCatching {
                                            LytroConnection().probe("10.100.1.1", 5678)
                                        }
                                    }
                                    status = result.fold(
                                        onSuccess = { "Port TCP 5678 odpowiada. Następny krok: test pakietu identyfikacyjnego." },
                                        onFailure = { "Brak połączenia: ${it.message ?: "nieznany błąd"}" }
                                    )
                                    busy = false
                                }
                            }
                        ) { Text(if (busy) "Łączenie…" else "Sprawdź aparat") }
                        Text(
                            "Uwaga: to jest szkielet projektu. Sam test portu nie odczytuje jeszcze zdjęć ani nie potwierdza zgodności z firmware aparatu.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
