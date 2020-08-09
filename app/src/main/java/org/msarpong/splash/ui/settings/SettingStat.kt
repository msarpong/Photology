package org.msarpong.splash.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Line
import com.anychart.data.Mapping
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import org.koin.android.ext.android.inject
import org.msarpong.splash.R
import org.msarpong.splash.service.mapping.stats.StatsResponse
import org.msarpong.splash.ui.collections.CollectionScreen
import org.msarpong.splash.ui.main.MainScreen
import org.msarpong.splash.ui.search.SearchPhotoScreen
import org.msarpong.splash.ui.user.UserScreen
import org.msarpong.splash.util.*
import org.msarpong.splash.util.sharedpreferences.KeyValueStorage

class SettingStat : AppCompatActivity() {

    private val prefs: KeyValueStorage by inject()
    private val viewModel: SettingsViewModel by inject()

    private lateinit var homeBtn: ImageButton
    private lateinit var collectionBtn: ImageButton
    private lateinit var searchBtn: ImageButton
    private lateinit var profileBtn: ImageButton
    private lateinit var settingBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_stat)
        initViews()
        setupViews()
    }

    private fun initViews() {
        settingBtn = findViewById(R.id.setting_btn)
        homeBtn = findViewById(R.id.home_btn)
        collectionBtn = findViewById(R.id.collection_btn)
        searchBtn = findViewById(R.id.search_btn)
        profileBtn = findViewById(R.id.profile_btn)
    }

    private fun setupViews() {
        if (!prefs.getString(ACCESS_TOKEN).isNullOrEmpty()) {
            settingBtn.visibility = View.VISIBLE
        }

        settingBtn.setColorFilter(ContextCompat.getColor(this, R.color.active_button))

        homeBtn.setOnClickListener {
            startActivity(Intent(this, MainScreen::class.java))
        }
        collectionBtn.setOnClickListener {
            startActivity(Intent(this, CollectionScreen::class.java))
        }
        searchBtn.setOnClickListener {
            startActivity(Intent(this, SearchPhotoScreen::class.java))
        }
        profileBtn.setOnClickListener {
            startActivity(Intent(this, UserScreen::class.java))
        }
        settingBtn.setOnClickListener {
            startActivity(Intent(this, SettingsScreen::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is SettingState.Error -> showError(state.error)
                is SettingState.SuccessStats -> showResult(state.stats)
            }
        })

        viewModel.send(SettingEvent.Stats(prefs.getString(USERNAME).toString()))
    }

    private fun showResult(stats: StatsResponse) {

        val anyChartView = findViewById<AnyChartView>(R.id.any_chart_view)
        val cartesian: Cartesian = AnyChart.line()

        cartesian.animation(false)

        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true) // TODO ystroke
            .yStroke(null as Stroke?, null, null, null as String?, null as String?)

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        val views = arrayOf(stats.views.historical.values)
        val downloads = arrayOf(stats.downloads.historical.values)
        val likes = arrayOf(stats.likes.historical.values)
        val max = stats.views.historical.values.size

        val likesArray = ArrayList<String>()
        val viewsArray = ArrayList<String>()
        val downloadsArray = ArrayList<String>()
        val dateArray = ArrayList<String>()
        for (element in likes) {
            element.forEach {
                val likeValue = it.value
                dateArray.add(it.date)
                likesArray.add(likeValue.toString())
            }
        }

        for (element in downloads) {
            element.forEach {
                val downloadValue = it.value
                downloadsArray.add(downloadValue.toString())
            }
        }

        for (element in views) {
            element.forEach {
                val viewsValue = it.value
                viewsArray.add(viewsValue.toString())
            }
        }
        Log.d("SettingStatAct", "TotalArray: ${likesArray.size}")

        for (i in likesArray) {
            Log.d("SettingStatAct", "TotalArrayElement: ${i}")
        }

        Log.d("SettingStatAct", "LikesStats: $likesArray")
        Log.d("SettingStatAct", "LikesStats: ${likesArray.size}")
        Log.d("SettingStatAct", "DownloadsStats: $downloadsArray")
        Log.d("SettingStatAct", "DownloadsStats: ${downloadsArray.size}")
        Log.d("SettingStatAct", "ViewsStats: $viewsArray")
        Log.d("SettingStatAct", "ViewsStats: ${viewsArray.size}")
        Log.d("SettingStatAct", "IteratorMax: $max")

        val seriesData: MutableList<DataEntry> = ArrayList()

        for (i in 0 until max) {
            seriesData.add(
                CustomDataEntry(
                    forDate(dateArray[i]),
                    likesArray[i].toInt(),
                    downloadsArray[i].toInt(),
                    viewsArray[i].toInt()
                )
            )

            Log.d(
                "SettingStatAct",
                "ArrayStats: ${dateArray[i]} = ${likesArray[i]}: ${downloadsArray[i]}: ${viewsArray[i]}"
            )

        }

        val set: Set = Set.instantiate()
        set.data(seriesData)
        val series1Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value' }")
        val series2Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value2' }")
        val series3Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value3' }")

        val series1: Line = cartesian.line(series1Mapping)
        series1.name("Likes")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        val series2: Line = cartesian.line(series2Mapping)
        series2.name("Downloads")
        series2.hovered().markers().enabled(true)
        series2.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series2.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        val series3: Line = cartesian.line(series3Mapping)
        series3.name("Views")
        series3.hovered().markers().enabled(true)
        series3.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series3.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

        anyChartView.setChart(cartesian)
    }

    private fun showError(error: Throwable) {
        Log.d("SettingStatAct", "showError: $error")
    }

    private class CustomDataEntry internal constructor(
        x: String?,
        value: Number?,
        value2: Number?,
        value3: Number?
    ) :
        ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
            setValue("value3", value3)
        }
    }
}
