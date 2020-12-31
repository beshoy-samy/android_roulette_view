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
    }

    private fun initRouletteItems(): List<RouletteWheelItemModel> =
        listOf(
            RouletteWheelItemModel(R.color.colorAccent),
            RouletteWheelItemModel(R.color.purple_700),
            RouletteWheelItemModel(R.color.teal),
            RouletteWheelItemModel(R.color.white),
            RouletteWheelItemModel(R.color.navi_red),
            RouletteWheelItemModel(R.color.navi_green)
        )

    override fun getLayoutResId(): Int = R.layout.activity_main

}