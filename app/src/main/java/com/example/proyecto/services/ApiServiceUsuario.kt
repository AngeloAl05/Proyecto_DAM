package com.example.proyecto.services

import com.example.proyecto.entidad.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServiceUsuario {

    @GET("/api/Usuario")
    fun findAll(): Call<List<Usuario>>

    @POST("/api/Usuario")
    fun save(@Body bean: Usuario): Call<Usuario>

    @GET("/api/Usuario/{codigo}")
    fun findById(@Path("codigo") cod: Int): Call<Usuario>

    @PUT("/api/Usuario")
    fun update(@Body bean: Usuario): Call<Usuario>

    @DELETE("/api/Usuario/{codigo}")
    fun deleteById(@Path("codigo") cod: Int): Call<Void>
}