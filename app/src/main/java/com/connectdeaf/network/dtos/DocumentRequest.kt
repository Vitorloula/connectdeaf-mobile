package com.connectdeaf.network.dtos

data class UploadDocumentRequest(
    val filename: String,
    val content: String // Conteúdo do arquivo em base64
)

data class VerifyDocumentRequest(
    val document_path: String, // Caminho do arquivo no storage
    val professional_name: String // Nome do profissional a ser comparado com o certificado
)