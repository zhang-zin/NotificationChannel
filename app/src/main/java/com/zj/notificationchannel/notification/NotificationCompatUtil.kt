package com.zj.notificationchannel.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotificationCompatUtil {

    companion object {

        /**
         * 创建通知
         *
         * [context]        上下文
         * [channel]        通知渠道
         * [title]          标题
         * [contentText]    正文文本
         * [intent]         对点击操作做出响应意图
         *
         */
        fun createNotificationBuilder(
            context: Context,
            channel: Channel,
            title: CharSequence? = null,
            contentText: CharSequence? = null,
            intent: Intent? = null
        ): NotificationCompat.Builder {
            // 要先创建通知渠道，才能在Android 8.0及更高版本上发布任何通知
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel(context, channel)
            }

            val builder = NotificationCompat.Builder(context, channel.channelId)
                .setPriority(getLowVersionPriority(channel))  // 通知优先级
                .setVisibility(channel.lockScreenVisibility)  // 锁定屏幕公开范围
                .setVibrate(channel.vibrate)                  // 振动模式
                .setSound(
                    channel.sound ?: Settings.System.DEFAULT_NOTIFICATION_URI
                )                       // 声音
                .setOnlyAlertOnce(true) // 设置通知只会在通知首次出现时打断用户（通过声音、振动或视觉提示），而之后更新则不会再打断用户。

            // 标题
            if (!TextUtils.isEmpty(title)) {
                builder.setContentTitle(title)
            }

            // 正文文本
            if (!TextUtils.isEmpty(contentText)) {
                builder.setContentText(contentText)
            }

            // 设置通知的点按操作，每个通知都应该对点按操作做出响应，通常是在应用中打开对应于该通知的Activity。
            if (intent != null) {
                val pendingIntent =
                    PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                builder.setContentIntent(pendingIntent)
                    .setAutoCancel(true) // 在用户点按通知后自动移除通知
                if (NotificationManager.IMPORTANCE_HIGH == channel.importance) builder.setFullScreenIntent(
                    pendingIntent,
                    false
                )
            }

            return builder
        }

        private fun getLowVersionPriority(channel: Channel): Int {
            return when (channel.importance) {
                NotificationManager.IMPORTANCE_HIGH -> NotificationCompat.PRIORITY_HIGH
                NotificationManager.IMPORTANCE_LOW -> NotificationCompat.PRIORITY_LOW
                NotificationManager.IMPORTANCE_MIN -> NotificationCompat.PRIORITY_MIN
                else -> NotificationCompat.PRIORITY_DEFAULT
            }
        }

        /**
         * 创建通知渠道
         * <p>
         * 反复调用这段代码也是安全的，因为创建现有通知渠道不会执行任何操作。
         * 注意：创建通知渠道后，您便无法更改通知行为，此时用户拥有完全控制权。不过，您仍然可以更改渠道的名称和说明。
         * [context] 上下文
         * [channel] 通知渠道
         */
        @RequiresApi(Build.VERSION_CODES.O)
        private fun createChannel(context: Context, channel: Channel) {
            val notificationChannel =
                NotificationChannel(channel.channelId, channel.name, channel.importance)
            notificationChannel.description = channel.description  // 描述
            notificationChannel.vibrationPattern = channel.vibrate // 振动模式
            notificationChannel.setSound(
                channel.sound ?: Settings.System.DEFAULT_NOTIFICATION_URI,
                notificationChannel.audioAttributes
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        /**
         * 取消通知
         * [context]       上下文
         * [id]            通知的唯一id
         * [notification]  通知
         */
        fun notify(context: Context, id: Int, notification: Notification) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(id, notification)
        }

        /**
         * 显示通知
         * 请记得保存您传递到 NotificationManagerCompat.notify() 的通知 ID，因为如果之后您想要更新或移除通知，将需要使用这个 ID。
         *
         * [context]   上下文
         * [id]        通知的唯一id
         */
        fun cancel(context: Context, id: Int) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(id)
        }

        /**
         * 取消所有通知
         * [context] 上下文
         */
        fun cancelAll(context: Context) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        }

    }


    /**
     * 通知渠道
     */
    data class Channel(
        val channelId: String,                                                       // 唯一渠道ID
        val name: CharSequence,                                                      // 用户可见名称
        val importance: Int,                                                         // 重要性级别
        val description: String? = null,                                             // 描述
        @NotificationCompat.NotificationVisibility
        val lockScreenVisibility: Int = NotificationCompat.VISIBILITY_SECRET,        // 锁定屏幕公开范围
        val vibrate: LongArray? = null,                                              // 震动模式
        val sound: Uri? = null                                                       // 声音
    )
}