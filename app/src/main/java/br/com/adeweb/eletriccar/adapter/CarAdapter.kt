package br.com.adeweb.eletriccar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.adeweb.eletriccar.R
import br.com.adeweb.eletriccar.databinding.CarroItemBinding
import br.com.adeweb.eletriccar.domain.Carro

class CarAdapter(private val carros: List<Carro>, private val isfavoriteScreen: Boolean = false) :
    RecyclerView.Adapter<CarAdapter.CarViewHolder>(){
    var carItemLister : (Carro) -> Unit = {}
    class CarViewHolder(val itemBinding: CarroItemBinding):
    RecyclerView.ViewHolder(itemBinding.root){


       // private val binding: CarroItemBinding
        init{
           itemBinding.tvValorVeiculo
           itemBinding.tvValorBateria
           itemBinding.tvValorPotencia
           itemBinding.tvValorRecargar
           itemBinding.ivFavorite

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        val itemCarBindig = CarroItemBinding.inflate(
            layoutInflater, parent, false
        )
        return CarViewHolder(itemCarBindig)
    }

    override fun getItemCount(): Int {
        return carros.size
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.itemBinding.tvValorVeiculo.text =  carros[position].preco
        holder.itemBinding.tvValorBateria.text =  carros[position].bateria
        holder.itemBinding.tvValorPotencia.text =  carros[position].potencia
        holder.itemBinding.tvValorRecargar.text =  carros[position].recarga
        holder.itemBinding.tvValorRecargar.text =  carros[position].recarga
        if (isfavoriteScreen) {
            holder.itemBinding.ivFavorite.setImageResource(R.drawable.ic_star_selected)
        }
        holder.itemBinding.ivFavorite.setOnClickListener{
            val carro = carros[position]
            carItemLister(carro)

            setupFavorite(carro, holder)
        }
    }

    private fun setupFavorite(
        carro: Carro,
        holder: CarViewHolder
    ) {

            carro.isFavorite = !carro.isFavorite
            if (carro.isFavorite)
                holder.itemBinding.ivFavorite.setImageResource(R.drawable.ic_star_selected)
            else
                holder.itemBinding.ivFavorite.setImageResource(R.drawable.ic_star)

    }


}



