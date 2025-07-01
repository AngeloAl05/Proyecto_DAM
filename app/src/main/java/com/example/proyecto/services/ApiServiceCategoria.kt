package com.example.proyecto.services

import com.example.proyecto.entidad.Categoria
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServiceCategoria {

    @GET("/api/Categoria")
    fun findAll(): Call<List<Categoria>>

    @POST("/api/Categoria")
    fun save(@Body bean: Categoria): Call<Categoria>

    @GET("/api/Categoria/{codigo}")
    fun findById(@Path("codigo") cod: Int): Call<Categoria>

    @PUT("/api/Categoria")
    fun update(@Body bean: Categoria): Call<Categoria>

    @DELETE("/api/Categoria/{codigo}")
    fun deleteById(@Path("codigo") cod: Int): Call<Void>
}