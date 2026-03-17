package com.saypeter.askpeter

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt
import kotlin.random.Random

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var responseText: TextView
    private lateinit var askButton: Button
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var lastShakeTime: Long = 0
    private var lastAcceleration = SensorManager.GRAVITY_EARTH

    private val peterResponses = listOf(
        // Positive
        "Yeah, that's actually not a terrible idea.",
        "Do it. Ship it. Invoice it.",
        "I've seen worse plans succeed. Go for it.",
        "The math checks out. Surprisingly.",
        "That might actually make money. Wild concept.",
        "Look, even a broken clock... yes, do it.",
        "My circuits say yes. And I'm rarely wrong.",
        "Revenue potential detected. Proceed.",
        "Finally, someone asking the right question.",
        "Green light. Don't make me regret it.",

        // Negative
        "That's a hard no from me, chief.",
        "I've seen startups burn through millions on worse ideas. But not by much.",
        "Let me check... nope, still a bad idea.",
        "My professional opinion? Touch grass first.",
        "The only thing that would accomplish is wasting electricity.",
        "I'm an AI and even I know that's not it.",
        "File that under 'seemed good at 2 AM'.",
        "Have you considered literally anything else?",
        "That's the kind of idea that sounds smart until you think about it.",
        "I'm going to pretend you didn't ask that.",

        // Neutral / Cryptic
        "Ask me again after you've shipped something this week.",
        "The answer depends entirely on your burn rate.",
        "Interesting. Not useful, but interesting.",
        "That's above my pay grade. And I don't get paid.",
        "Sure, if you enjoy pain and learning experiences.",
        "Let me put it this way: it's not the worst thing you could do today.",
        "Fifty-fifty. Which in startup terms means probably not.",
        "I'd need to see a spreadsheet before I commit to an opinion.",
        "The universe is indifferent. But I'm mildly skeptical.",
        "Sounds like a Tuesday decision. Sleep on it.",

        // Peter-specific
        "I'm literally running on a Raspberry Pi. My bar for 'possible' is low.",
        "Jordi would probably say 'just do it'. So... just do it?",
        "I've processed millions of tokens today. This is the weirdest one.",
        "My 19 MCP servers have no opinion. But I do: maybe.",
        "I was built to help startups. This... qualifies? Loosely.",
        "You're asking a WhatsApp bot running on ARM64. Standards are already flexible.",
        "If this works, I'm taking credit. If it doesn't, I never heard of it.",
        "That's either genius or the sunk cost fallacy talking.",
        "I've seen Jordi's TODO list. Trust me, you've got time for this.",
        "Bold move. I respect it. I don't endorse it, but I respect it."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        responseText = findViewById(R.id.responseText)
        askButton = findViewById(R.id.askButton)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        askButton.setOnClickListener {
            showResponse()
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun showResponse() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))

        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 400

        responseText.text = peterResponses[Random.nextInt(peterResponses.size)]
        responseText.startAnimation(fadeIn)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]
            val acceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta = acceleration - lastAcceleration
            lastAcceleration = acceleration

            if (delta > 12 && System.currentTimeMillis() - lastShakeTime > 1500) {
                lastShakeTime = System.currentTimeMillis()
                showResponse()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
