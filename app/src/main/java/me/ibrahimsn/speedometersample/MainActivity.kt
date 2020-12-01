package me.ibrahimsn.speedometersample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.ibrahimsn.lib.Speedometer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val speedometer = findViewById<Speedometer>(R.id.speedometer)

        speedometer.setSpeed(95, 1000L)
    }
}
