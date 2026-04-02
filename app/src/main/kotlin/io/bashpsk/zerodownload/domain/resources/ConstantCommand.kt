package io.bashpsk.zerodownload.domain.resources

object ConstantCommand {

    const val DOWNLOADER_OPTION = "--downloader"
    const val DOWNLOADER_ARGS = "libaria2c.so"

    const val EXTERNAL_DOWNLOADER_OPTION = "--external-downloader-args"
    const val EXTERNAL_DOWNLOADER_ARGS = "aria2c:\"--summary-interval=1\""

    const val MEDIA_TITLE_EXT_DEFAULT = "%(title)s.%(ext)s"

    const val FILE_NAME_PATTERN = "[^A-Za-z0-9%&.()\\[\\]_\\-\\s]"
}