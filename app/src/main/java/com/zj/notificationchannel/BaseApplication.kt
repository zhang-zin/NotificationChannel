package com.zj.notificationchannel

import android.app.Application
import android.content.Context

class BaseApplication : Application() {

    companion object {
        private lateinit var mContext: Context
        fun getContext(): Context {
            return mContext
        }
    }

    init {
        mContext = this
    }
}