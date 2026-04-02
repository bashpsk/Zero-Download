package io.bashpsk.zerodownload.ytdlext.mapper

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class PlaylistVideoInfo {

    val id: String? = null
    val fulltitle: String? = null
    val title: String? = null

    val duration: Int = 0
    val thumbnail: String? = null

    @JsonProperty("uploader_id")
    val uploaderId: String? = null
    val uploader: String? = null

    @JsonProperty("player_url")
    val playerUrl: String? = null

    @JsonProperty("webpage_url")
    val webpageUrl: String? = null

    @JsonProperty("manifest_url")
    val manifestUrl: String? = null
    val url: String? = null

    @JsonProperty("playlist_index")
    val playlistIndex: Int? = null
}