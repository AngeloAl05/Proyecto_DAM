package com.example.proyecto.entidad

class Categoria( var idCategoria: Int = 0,
                 var nombreCate: String) {
    override fun toString(): String {
        return nombreCate
    }
}