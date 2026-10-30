package pl.lytromobile.image

import java.io.File
import java.io.RandomAccessFile

/**
 * Wstępna kontrola nagłówka komponentu LFP.
 * Bajt 3 oznacza typ komponentu: P (package), M (metadata) lub C (component).
 */
object LfpParser {
    private val prefix = byteArrayOf(
        0x89.toByte(), 0x4C, 0x46
    )
    private val suffix = byteArrayOf(
        0x0D, 0x0A, 0x1A, 0x0A
    )

    fun hasLfpSignature(file: File): Boolean {
        if (!file.isFile || file.length() < 16) return false
        RandomAccessFile(file, "r").use { raf ->
            val header = ByteArray(8)
            raf.readFully(header)
            val validType = header[3] == 'P'.code.toByte() ||
                header[3] == 'M'.code.toByte() ||
                header[3] == 'C'.code.toByte()
            return header.copyOfRange(0, 3).contentEquals(prefix) &&
                validType &&
                header.copyOfRange(4, 8).contentEquals(suffix)
        }
    }
}
