package com.example.proyecto.utils

import com.example.proyecto.services.ApiServiceCategoria
import com.example.proyecto.services.ApiServiceUsuario

class ApiUtils {
    companion object {
        private const val BASE_URL = "http://mundocandyuwu.somee.com/swagger/index.html"

        fun getAPIUsuario(): ApiServiceUsuario {
            return RetrofitClient.getClient(BASE_URL).create(ApiServiceUsuario::class.java)
        }
        fun getAPICategoria(): ApiServiceCategoria {
            return RetrofitClient.getClient(BASE_URL).create(ApiServiceCategoria::class.java)
        }

    }
}
