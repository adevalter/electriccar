package br.com.adeweb.eletriccar.data

import br.com.adeweb.eletriccar.domain.Carro

object CarFactory {

    val list = listOf<Carro>(
        Carro(
            id = 1,
            preco = "R$ 3000.000.00",
            bateria = "300 Kwh",
            potencia = "200 cv",
            recarga = "30 min",
            urlPhoto = "www.google.com.br",
             isFavorite = false
        ),
        Carro(
            id = 2,
            preco = "R$ 2000.000.00",
            bateria = "200 Kwh",
            potencia = "400 cv",
            recarga = "60 min",
            urlPhoto = "www.google.com.br",
            isFavorite = false
        )
    )
}