package com.wasingun.seller_lounge.ui.trendcomparison

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordResult
import com.wasingun.seller_lounge.databinding.FragmentTrendComparisonResultBinding
import com.wasingun.seller_lounge.ui.BaseFragment

class TrendComparisonResultFragment : BaseFragment<FragmentTrendComparisonResultBinding>() {
    private val args: TrendComparisonResultFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lineChart = binding.chart
        val lineData = LineData()
        val keywordResult = args.keywordResponse.results
        val dateList = keywordResult[0].data.map { removeYear(it.period) }

        setAxis(lineChart, dateList)
        setChartData(keywordResult, lineData)
        setChartDescription(lineChart, lineData)
    }

    private fun setChartDescription(
        lineChart: LineChart,
        lineData: LineData
    ) {
        with(lineChart) {
            data = lineData
            setVisibleXRangeMaximum(7f)
            description.isEnabled = false
        }
    }

    private fun setChartData(
        keywordResult: List<KeywordResult>,
        lineData: LineData
    ) {
        for (i in keywordResult.indices) {
            val colorList = listOf(Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA, Color.CYAN)
            val entries = mutableListOf<Entry>()
            val title = keywordResult[i].title
            val keywordDataRatio = keywordResult[i].data

            for (j in keywordDataRatio.indices) {
                entries.add(Entry(j.toFloat(), keywordDataRatio[j].ratio.toFloat()))
            }

            val dataSet = LineDataSet(entries, title)
            dataSet.color = colorList[i]
            lineData.addDataSet(dataSet)
        }
    }

    private fun setAxis(
        lineChart: LineChart,
        dateList: List<String>
    ) {
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(dateList)
        xAxis.labelCount = dateList.size
        xAxis.granularity = 1f
        xAxis.axisLineColor = Color.BLACK

        val yAxis = lineChart.axisLeft
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 100f
        yAxis.axisLineWidth = 2f
        yAxis.axisLineColor = Color.BLACK
        yAxis.labelCount = 10
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_trend_comparison_result
    }

    private fun removeYear(date: String): String {
        var changedDateFormat = ""
        for (i in 5..9) {
            changedDateFormat += date[i]
        }
        return changedDateFormat
    }
}