package com.example.ban_giay_mobile.activity

import android.content.Intent
import android.os.Bundle
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.databinding.ActivityIntroBinding


class IntroActivity : BaseActivity() {
    private lateinit  var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}