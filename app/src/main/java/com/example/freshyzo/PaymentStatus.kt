package com.example.freshyzo

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PaymentStatus : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_status)

        val imageView = findViewById<ImageView>(R.id.paymentStatusImageView)

        val intentData = intent.getStringExtra("status")
        if(intentData.equals("success")){
            imageView.setImageResource(R.drawable.icon_circle_check)
        }
        else if (intentData.equals("failed")){
            imageView.setImageResource(R.drawable.icon_circle_cross)
        }

        val scaleAnimation = ScaleAnimation(
            1.0f,  // Start scale X
            1.5f,  // End scale X (1.5 times the original size)
            1.0f,  // Start scale Y
            1.5f,  // End scale Y (1.5 times the original size)
            Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point X (center of the view)
            Animation.RELATIVE_TO_SELF, 0.5f   // Pivot point Y (center of the view)
        )

        scaleAnimation.duration = 1000
        scaleAnimation.fillAfter = true
        imageView.startAnimation(scaleAnimation)



        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("returned", "home")
        startActivity(intent)
    }
}