package br.com.adeweb.eletriccar.data


import br.com.adeweb.eletriccar.domain.Carro
import retrofit2.Call
import retrofit2.http.GET

interface CarsApi {

    @GET("cars.json")
    fun getAllCars() : Call<List<Carro>>

}