package com.example.eco_pjt

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Kakao SDK
        KakaoSdk.init(this, "02fdcfd330b64798b7cc7b900ebf0054") // Replace with your app key
    }
}