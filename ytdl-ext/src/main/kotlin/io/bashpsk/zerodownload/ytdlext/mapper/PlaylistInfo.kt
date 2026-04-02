package io.bashpsk.zerodownload.ytdlext.mapper

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class PlaylistInfo {

    val id: String? = null
    val title: String? = null
    val uploader: String? = null

    @JsonProperty("uploader_id")
    val uploaderId: String? = null

    @JsonProperty("webpage_url")
    val webpageUrl: String? = null

    val description: String? = null

    val extractor: String? = null

    @JsonProperty("extractor_key")
    val extractorKey: String? = null

    val entries: ArrayList<PlaylistVideoInfo>? = null
}