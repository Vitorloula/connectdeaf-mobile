package com.connectdeaf.network.services

import com.connectdeaf.network.dtos.City
import com.connectdeaf.network.dtos.State
import retrofit2.http.GET
import retrofit2.http.Path

interface IBGEService {
    @GET("localidades/estados")
    suspend fun getStates(): List<State>

    @GET("localidades/estados/{state}/municipios")
    suspend fun getCities(@Path("state") state: String): List<City>
}
