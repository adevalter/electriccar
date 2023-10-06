package br.com.adeweb.eletriccar.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.adeweb.eletriccar.ui.CarFragment
import br.com.adeweb.eletriccar.ui.FavorieFragment

class TabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
         return when(position) {
             0 -> CarFragment()
             1 -> FavorieFragment()
             else ->  CarFragment()
         }


    }
}