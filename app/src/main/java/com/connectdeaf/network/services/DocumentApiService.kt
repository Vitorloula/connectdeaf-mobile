package com.connectdeaf.network.services

import com.connectdeaf.network.dtos.UploadDocumentRequest
import com.connectdeaf.network.dtos.UploadDocumentResponse
import com.connectdeaf.network.dtos.VerifyDocumentRequest
import com.connectdeaf.network.dtos.VerifyDocumentResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface DocumentApiService {
    @POST("api/documents/upload_file")
    suspend fun uploadFile(@Body request: UploadDocumentRequest): UploadDocumentResponse

    @POST("api/documents/verify_file")
    suspend fun verifyFile(@Body request: VerifyDocumentRequest): VerifyDocumentResponse
}