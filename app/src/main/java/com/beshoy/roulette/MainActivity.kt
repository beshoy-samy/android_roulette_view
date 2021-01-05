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
            RouletteWheelItemModel(R.color.colorAccent, "Beshoy"),
            RouletteWheelItemModel(R.color.purple_700, "Beshoy"),
            RouletteWheelItemModel(R.color.teal, "Beshoy"),
            RouletteWheelItemModel(R.color.white, "Beshoy"),
            RouletteWheelItemModel(R.color.navi_red, "Beshoy"),
            RouletteWheelItemModel(R.color.navi_green, "Beshoy"),
            RouletteWheelItemModel(R.color.colorAccent, "Beshoy"),
            RouletteWheelItemModel(R.color.purple_700, "Beshoy"),
            RouletteWheelItemModel(R.color.teal, "Beshoy"),
            RouletteWheelItemModel(R.color.white, "Beshoy"),
            RouletteWheelItemModel(R.color.navi_red, "Beshoy"),
            RouletteWheelItemModel(R.color.navi_green, "Beshoy")
        )

    override fun getLayoutResId(): Int = R.layout.activity_main

}