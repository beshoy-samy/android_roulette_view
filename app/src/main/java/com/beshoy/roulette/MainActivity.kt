package com.beshoy.roulette

import android.os.Bundle
import com.beshoy.roulette.base.BaseActivity
import com.beshoy.roulette.databinding.ActivityMainBinding
import com.beshoy.roulette.models.RouletteWheelItemModel

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun bindViews(savedInstanceState: Bundle?) {
        binding.drawBtn.setOnClickListener {
            val rouletteItems = initRouletteItems()
            binding.rouletteWheel.setRouletteWheelItems(rouletteItems)
        }

        binding.rouletteWheel.setSpinButtonClickListener {
            binding.rouletteWheel.spinTheWheel()
        }

        binding.resetBtn.setOnClickListener {
            binding.rouletteWheel.reset()
        }
    }

    private fun initRouletteItems(): List<RouletteWheelItemModel> =
        listOf(
            RouletteWheelItemModel(R.color.colorAccent, "10$"),
            RouletteWheelItemModel(R.color.purple_700, "20$"),
            RouletteWheelItemModel(R.color.teal, "30$"),
            RouletteWheelItemModel(R.color.white, "40$"),
            RouletteWheelItemModel(R.color.navi_red, "50$"),
            RouletteWheelItemModel(R.color.navi_green, "60$"),
            RouletteWheelItemModel(R.color.colorAccent, "70$"),
            RouletteWheelItemModel(R.color.purple_700, "80$"),
            RouletteWheelItemModel(R.color.teal, "90$"),
            RouletteWheelItemModel(R.color.white, "100$"),
            RouletteWheelItemModel(R.color.navi_red, "110$"),
            RouletteWheelItemModel(R.color.navi_green, "120$")
        )

    override fun getLayoutResId(): Int = R.layout.activity_main

}