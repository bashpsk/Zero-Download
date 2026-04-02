package io.bashpsk.zerodownload.data.extension

import android.util.Log
import io.bashpsk.zerodownload.domain.utils.LOG_TAG
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipInputStream

fun hasArchiveNeedPassword(path: String): Boolean? {

    return try {

        FileInputStream(path).use { fileInputStream ->

            ZipInputStream(fileInputStream).use { zipInputStream ->

                zipInputStream.nextEntry
                zipInputStream.nextEntry
            }
        }

        true
    } catch (exception: Exception) {

        null
    }
}

fun hasArchiveSplit(path: String): Boolean? {

    return try {

        val sourceFile = File(path)

        return sourceFile.parentFile?.listFiles()?.any { partFile ->

            val fileNameRegex = Regex(
                pattern = "${Regex.escape(sourceFile.name)}\\.z\\d{2}",
                option = RegexOption.IGNORE_CASE
            )

            sourceFile.path != partFile.path && partFile.name.matches(fileNameRegex)
        } == true
    } catch (exception: Exception) {

        null
    }
}

fun validatePartFile(path: String, partNumber: Int): File {

    return try {

        val sourceFile = File(path)
        val nameTemplate = "${sourceFile.nameWithoutExtension} - Part.${sourceFile.extension}.z%02d"

        File(sourceFile.parent, nameTemplate.format(partNumber))
    } catch (exception: Exception) {

        Log.e(LOG_TAG, exception.message, exception)
        File(path)
    }
}