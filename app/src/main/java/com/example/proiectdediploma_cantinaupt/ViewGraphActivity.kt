package com.example.proiectdediploma_cantinaupt

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.firestore.FirebaseFirestore

class ViewGraphActivity : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private lateinit var firestore: FirebaseFirestore

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_graph)

        barChart = findViewById(R.id.barChart)
        firestore = FirebaseFirestore.getInstance()

        fetchOrderData()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun fetchOrderData() {
        firestore.collection("selected_menus").get()
            .addOnSuccessListener { querySnapshot ->
                val foodCountMap = mutableMapOf<String, Int>()

                for (document in querySnapshot.documents) {
                    val mainCourse = document.getString("mainCourse") ?: ""
                    val sideDish = document.getString("sideDish") ?: ""
                    val salad = document.getString("salad") ?: ""

                    foodCountMap[mainCourse] = foodCountMap.getOrDefault(mainCourse, 0) + 1
                    foodCountMap[sideDish] = foodCountMap.getOrDefault(sideDish, 0) + 1
                    foodCountMap[salad] = foodCountMap.getOrDefault(salad, 0) + 1
                }

                val barEntries = ArrayList<BarEntry>()
                val foodNames = ArrayList<String>()
                var index = 0

                for ((food, count) in foodCountMap) {
                    barEntries.add(BarEntry(index.toFloat(), count.toFloat()))
                    foodNames.add(food)
                    index++
                }

                val barDataSet = BarDataSet(barEntries, "Food Orders")
                barDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
                val barData = BarData(barDataSet)

                barChart.data = barData
                barChart.xAxis.valueFormatter = IndexAxisValueFormatter(foodNames)
                barChart.xAxis.granularity = 1f
                barChart.xAxis.isGranularityEnabled = true
                barChart.xAxis.labelRotationAngle = -45f
                barChart.invalidate()
            }
            .addOnFailureListener { exception ->

            }
    }
}
