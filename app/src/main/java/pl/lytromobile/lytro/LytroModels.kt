package pl.lytromobile.lytro

data class LytroCameraInfo(
    val manufacturer: String,
    val serialNumber: String,
    val buildId: String,
    val softwareVersion: String
)

data class LytroPhoto(
    val id: String,
    val folder: String,
    val filePrefix: String,
    val folderNumber: Int,
    val fileNumber: Int,
    val isFavorite: Boolean,
    val lastFocusLambda: Float,
    val capturedAt: String,
    val rotationCode: Int
)
