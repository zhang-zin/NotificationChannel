package com.zj.notificationchannel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zj.notificationchannel.notification.PushNotificationHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PushNotificationHelper.notifyMessage(this, 1, "mdd", "新消息")

        //PushNotificationHelper.notifyMention(this, 2, "MadChan", "这是一条@提醒消息")
    }
}