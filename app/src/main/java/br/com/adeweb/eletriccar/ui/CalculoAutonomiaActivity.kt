package br.com.adeweb.eletriccar.ui

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.adeweb.eletriccar.R
import br.com.adeweb.eletriccar.databinding.ActivityCalculoAutonomiaBinding
import java.net.HttpURLConnection
import java.net.URL

class CalculoAutonomiaActivity : AppCompatActivity() {
    private  val binding by lazy {
        ActivityCalculoAutonomiaBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root)
        setupListeners()
        setupCacheResult()
    }

    private fun setupCacheResult() {
        val valorCalculado = getSharedPref()
        binding.txtResultado.text = valorCalculado.toString()
    }

    fun setupListeners(){
        binding.btnCalcularKm.setOnClickListener {
            calcular()
        }

        binding.ivClose.setOnClickListener{
            finish()
        }
    }

    fun calcular(){
        val preco = binding.etPrecoKm.text.toString().toFloat()
        val km = binding.etKmPercorrido.text.toString().toFloat()
        val result = preco/km

        binding.txtResultado.text = result.toString()
        saveSharedPref(result)
    }

    fun saveSharedPref(resultado: Float){
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putFloat(getString(R.string.saved_calc), resultado)
            apply()
        }
    }

    fun getSharedPref(): Float{
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getFloat(getString(R.string.saved_calc),0.0F)
    }
}