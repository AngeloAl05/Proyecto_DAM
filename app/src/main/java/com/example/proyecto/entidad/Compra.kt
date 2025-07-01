package com.example.proyecto.entidad

class Compra(
    var nombreProduc: String = "",
    var cantidadProduc: Int = 0,
    var precioProduc : Double = 0.0,
    var foto : String = "")
{

    constructor() : this("", 0, 0.0)
}
