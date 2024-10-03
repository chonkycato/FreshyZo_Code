package com.example.freshyzo.model

import com.google.gson.annotations.SerializedName

data class ColonyListResponse(
    val colony: List<ColonyResponse>
)

data class ColonyResponse(
    @SerializedName("colony_id") val colonyID: Int,    // Area ID
    @SerializedName("colony_name") val colonyName: String // Area Name
)
