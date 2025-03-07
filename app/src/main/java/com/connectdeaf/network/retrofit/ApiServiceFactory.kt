package com.connectdeaf.network.retrofit

import android.content.Context
import com.connectdeaf.data.repository.AuthRepository
import com.connectdeaf.network.services.ApiService
import com.connectdeaf.network.services.AppointmentService
import com.connectdeaf.network.services.AssessmentService
import com.connectdeaf.network.services.DocumentApiService
import com.connectdeaf.network.services.FAQApiService
import com.connectdeaf.network.services.IBGEService
import com.connectdeaf.network.services.PaymentService
import com.connectdeaf.network.services.ProfessionalService
import com.connectdeaf.network.services.ServiceService
import com.connectdeaf.network.services.UserService
import retrofit2.Retrofit

class ApiServiceFactory(context: Context) {

    private val authRepository = AuthRepository(context)

    private val getToken: () -> String? = {
        authRepository.getAuthToken()
    }

    private val sharedRetrofit: Retrofit = RetrofitInstance.createRetrofit("https://connectdeaf-backend-dev.azurewebsites.net/", getToken)

    private val ibgeRetrofit: Retrofit = RetrofitInstance.createRetrofit("https://servicodados.ibge.gov.br/api/v1/", getToken)

    private val aiRetrofit: Retrofit = RetrofitInstance.createRetrofit("https://webapp-connectdeaf-dev.azurewebsites.net/", getToken)

    val apiService: ApiService by lazy {
        sharedRetrofit.create(ApiService::class.java)
    }

    val documentApiService by lazy {
        aiRetrofit.create(DocumentApiService::class.java)
    }

    val professionalService: ProfessionalService by lazy {
        sharedRetrofit.create(ProfessionalService::class.java)
    }

    val userService: UserService by lazy {
        sharedRetrofit.create(UserService::class.java)
    }

    val serviceService: ServiceService by lazy {
        sharedRetrofit.create(ServiceService::class.java)
    }

    val ibgeService: IBGEService by lazy {
        ibgeRetrofit.create(IBGEService::class.java)
    }

    val faqApiService: FAQApiService by lazy {
        aiRetrofit.create(FAQApiService::class.java)
    }

    val appointmentService: AppointmentService by lazy {
        sharedRetrofit.create(AppointmentService::class.java)
    }

    val assessmentService: AssessmentService by lazy {
        sharedRetrofit.create(AssessmentService::class.java)

    }

    val paymentService: PaymentService by lazy {
        sharedRetrofit.create(PaymentService::class.java)
    }
}
