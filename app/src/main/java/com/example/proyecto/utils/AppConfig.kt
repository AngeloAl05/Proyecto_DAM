package com.example.proyecto.utils

import android.app.Application
import android.content.Context
import com.example.proyecto.data.InitBD

class AppConfig : Application(){

    companion object{
        lateinit var CONTEXTO: Context
        lateinit var BD: InitBD
        var NOMBREBD="proyecto.bd"
        var VERSION=1

        const val Usuario = "Usuario"
        const val UserId = "UserId"
        const val Username = "Username"
        const val Apellido = "Apellido"
        const val Password = "Password"
        const val Email = "Email"
        const val Tipo = "Tipo"
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXTO = applicationContext
        BD = InitBD()
        //FacebookSdk.sdkInitialize(applicationContext)
        //AppEventsLogger.activateApp(this)
    }
}