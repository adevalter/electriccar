package br.com.adeweb.eletriccar.ui

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import br.com.adeweb.eletriccar.R
import br.com.adeweb.eletriccar.adapter.CarAdapter
import br.com.adeweb.eletriccar.data.CarsApi
import br.com.adeweb.eletriccar.data.local.CarRepository
import br.com.adeweb.eletriccar.data.local.CarrosContract
import br.com.adeweb.eletriccar.data.local.CarrosContract.CarEntry.COLUMN_NAME_BATERIA
import br.com.adeweb.eletriccar.data.local.CarrosContract.CarEntry.COLUMN_NAME_POTENCIA
import br.com.adeweb.eletriccar.data.local.CarrosContract.CarEntry.COLUMN_NAME_PRECO
import br.com.adeweb.eletriccar.data.local.CarrosContract.CarEntry.COLUMN_NAME_RECARGA
import br.com.adeweb.eletriccar.data.local.CarrosContract.CarEntry.COLUMN_NAME_URLPHOTO
import br.com.adeweb.eletriccar.data.local.CarrosContract.CarEntry.TABLE_NAME
import br.com.adeweb.eletriccar.data.local.CarsDbHelper
import br.com.adeweb.eletriccar.databinding.CarFragmentBinding
import br.com.adeweb.eletriccar.domain.Carro
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.net.URL

class CarFragment : Fragment() {
    private var _binding:CarFragmentBinding? = null
    private val binding get() = _binding!!
    var carrosArray: ArrayList<Carro> = ArrayList()
    lateinit var carsApi: CarsApi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CarFragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRetrofit()
        setupListeners()


    }

    override fun onResume() {
        super.onResume()
        if(checkForInternet(context)) {
           // callService() -> outra forma de chamar serviço
            getAllCars()
        }else{
            binding.pbLoader.isVisible = false
            binding.rvListaCarros.isVisible = false
            binding.ivEmptyState.isVisible = true
            binding.tvNoWifi.isVisible = true
        }
    }

    fun setupRetrofit(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://igorbag.github.io/cars-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        carsApi = retrofit.create(CarsApi::class.java)
    }

    fun getAllCars(){
        carsApi.getAllCars().enqueue(object : Callback<List<Carro>>{
            override fun onResponse(call: Call<List<Carro>>, response: Response<List<Carro>>) {
                if(response.isSuccessful){

                    binding.pbLoader.isVisible = false
                    binding.ivEmptyState.isVisible = false
                    binding.tvNoWifi.isVisible = false
                    response.body()?.let {
                        setupList(it)
                    }

                }else{
                    Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Carro>>, t: Throwable) {
                Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
            }

        })
    }

    fun setupList(lista: List<Carro>){
        binding.rvListaCarros.isVisible = true
        val adapter = CarAdapter(lista)
        binding.rvListaCarros.adapter = adapter
        adapter.carItemLister = { carro ->
           val bateria = carro.bateria
           val isSaved = CarRepository(requireContext()).saveIFNotExist(carro)
            //Criar Delete car

        }
    }
    fun setupListeners(){
        binding.fabCalcular.setOnClickListener {

          startActivity(Intent(context, CalculoAutonomiaActivity::class.java))
        }
    }

    fun callService(){
        val urlBase = "https://igorbag.github.io/cars-api/cars.json"
        MyTask().execute(urlBase)

    }

    fun checkForInternet(context: Context? ): Boolean{
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network= connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                else -> false
            }
        }else{
            @Suppress("DEPRECATION")
          val networkInfo =  connectivityManager.activeNetworkInfo?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
    //Usar Retrofit
    inner class MyTask : AsyncTask<String, String, String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("MyTask", "Iniciando")
            binding.pbLoader.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg url: String?): String {
            var urlConnectio: HttpURLConnection? = null
            try {
                val urlBase = URL(url[0])
                urlConnectio = urlBase.openConnection() as HttpURLConnection
                urlConnectio.connectTimeout = 60000
                urlConnectio.readTimeout = 60000
                urlConnectio.setRequestProperty(
                    "Accept",
                    "application/json"
                )

                val responseCode = urlConnectio.responseCode

                if(responseCode == HttpURLConnection.HTTP_OK){
                    var response = urlConnectio.inputStream.bufferedReader().use { it.readText() }
                    publishProgress(response)
                }else{
                    Log.e("error", "Servico Indisponivel")
                }


            }catch (ex:java.lang.Exception ){
                Log.e("Error", "Erro ao realizar processamento ....")


        }finally{

                urlConnectio?.disconnect()

        }
            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {

                try {
                    var jsonArray = JSONTokener(values[0]).nextValue() as JSONArray
                    for (i in 0 until jsonArray.length()){
                        val id = jsonArray.getJSONObject(i).getString("id")
                        val preco = jsonArray.getJSONObject(i).getString("preco")
                        val bateria = jsonArray.getJSONObject(i).getString("bateria")
                        val potencia = jsonArray.getJSONObject(i).getString("potencia")
                        val recarga = jsonArray.getJSONObject(i).getString("recarga")
                        val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")

                        val model = Carro(
                            id = id.toInt(),
                            preco = preco,
                            bateria =bateria,
                            potencia = potencia,
                            recarga = recarga,
                            urlPhoto = urlPhoto,
                            isFavorite = false
                        )
                        carrosArray.add(model)
                    }
                    binding.pbLoader.isVisible = false
                    binding.ivEmptyState.isVisible = false
                    binding.tvNoWifi.isVisible = false
                    //setupList()

            }catch (ex: Exception){

            }
        }



  /*      fun streamToString(inputStream: InputStream): String{
            val bufferReader = BufferedReader(InputStreamReader(inputStream))
            var line: String
            var result = ""

            try {
                do {
                    line = bufferReader.readLines().toString()
                    line?.let{
                        result += line
                    }
                }while (line != null)
            }catch (ex: Exception){
                Log.e("Erro", "erro ao parcelar Stream")
            }
            return result
        }*/
    }

}


