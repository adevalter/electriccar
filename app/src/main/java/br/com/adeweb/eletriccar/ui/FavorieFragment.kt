package br.com.adeweb.eletriccar.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import br.com.adeweb.eletriccar.adapter.CarAdapter
import br.com.adeweb.eletriccar.data.local.CarRepository
import br.com.adeweb.eletriccar.databinding.FavoriteFragamentBinding
import br.com.adeweb.eletriccar.domain.Carro

class FavorieFragment : Fragment() {
    private var _binding: FavoriteFragamentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FavoriteFragamentBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()
    }

    private fun getCarsOnLocalDb(): List<Carro> {

        val repository = CarRepository(requireContext())
        val carlist = repository.getAll()
        return carlist
    }

    fun setupList(){
        val cars = getCarsOnLocalDb()
        val carFavorites = binding.rvListaCarrosFavoritos
        val alertBuilder = AlertDialog.Builder(requireContext())
        alertBuilder.setTitle("Confirma Exclusão")
        alertBuilder.setMessage("Deseja realmente excluir o carro de Favoritos")

        carFavorites.isVisible = true

        val adapter = CarAdapter(cars, isfavoriteScreen = true)

        carFavorites.adapter = adapter
        adapter.carItemLister = { carro ->
            Log.d("setupList", "setupList: clicou")
            alertBuilder.setPositiveButton("sim") {_,_ ->
                val isSaved = CarRepository(requireContext()).removeCar(carro.id)
                if(isSaved){
                    setupList()
                    Toast.makeText(requireContext(),"Sucesso ao remover o carro de favoritosd", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(requireContext(),"Erro ao remover o carro de favoritosd", Toast.LENGTH_LONG).show()
                }
            }
            alertBuilder.setNegativeButton("Não"){ _, _ ->}

            alertBuilder.create().show()



        }
    }

}