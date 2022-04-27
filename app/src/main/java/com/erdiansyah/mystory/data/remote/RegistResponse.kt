package com.erdiansyah.mystory.data.remote

import com.google.gson.annotations.SerializedName

data class RegistResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

