package com.connectdeaf.network.dtos


import com.google.gson.annotations.SerializedName

data class UploadDocumentResponse(
    @SerializedName("response")
    val response: String // Ex: "documents/file.pdf"
)

data class VerifyDocumentResponse(
    @SerializedName("response")
    val response: String // Ex: "Válido" ou "Inválido"
)
