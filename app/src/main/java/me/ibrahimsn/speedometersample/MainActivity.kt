package me.ibrahimsn.speedometersample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import me.ibrahimsn.speedometer.Speedometer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val speedometer = findViewById<Speedometer>(R.id.speedometer)

        speedometer.setSpeed(95, 1000L)
    }
}
