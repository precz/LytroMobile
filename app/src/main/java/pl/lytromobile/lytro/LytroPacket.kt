package pl.lytromobile.lytro

import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Ramka protokołu opisana w społecznościowej dokumentacji Lytro.
 * Nagłówek ma 28 bajtów:
 * magic[4], length[4], flags[4], command[2], parameters[14].
 *
 * Uwaga: semantykę flag i wielkość bufora trzeba potwierdzić testami
 * na konkretnym firmware. Nie wysyłamy żadnych komend modyfikujących aparat.
 */
data class LytroPacket(
    val length: Int,
    val flags: Int,
    val command: Int,
    val parameters: ByteArray = ByteArray(14),
    val payload: ByteArray = byteArrayOf()
) {
    fun encode(): ByteArray {
        require(parameters.size == 14) { "Parametry nagłówka muszą mieć 14 bajtów" }
        val header = ByteBuffer.allocate(HEADER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
        header.put(MAGIC)
        header.putInt(length)
        header.putInt(flags)
        header.putShort(command.toShort())
        header.put(parameters)
        return header.array() + payload
    }

    companion object {
        const val HEADER_SIZE = 28
        val MAGIC = byteArrayOf(0xAF.toByte(), 0x55, 0xAA.toByte(), 0xFA.toByte())

        fun newReadRequest(command: Int, subCommand: Int, maxResponseBytes: Int): LytroPacket {
            require(maxResponseBytes in 1..(2 * 1024 * 1024)) {
                "Bufor odpowiedzi musi mieścić się w zakresie 1 B – 2 MiB"
            }
            val params = ByteArray(14)
            params[0] = subCommand.toByte()
            // Bit 0: length oznacza pojemność bufora; bit 1: request.
            return LytroPacket(
                length = maxResponseBytes,
                flags = 0x00000001,
                command = command,
                parameters = params
            )
        }
    }
}
