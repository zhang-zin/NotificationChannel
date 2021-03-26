package com.zj.notificationchannel.notification

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.zj.notificationchannel.BaseApplication
import com.zj.notificationchannel.MainActivity
import com.zj.notificationchannel.R

object PushNotificationHelper {

    /**
     * 通知渠道-聊天消息(重要性级别-高：发出声音)
     * */
    private val MESSAGE = NotificationCompatUtil.Channel(
        channelId = "MESSAGE",
        name = BaseApplication.getContext().getString(R.string.channel_message),
        importance = NotificationManagerCompat.IMPORTANCE_DEFAULT
    )

    /**
     * 通知渠道-@提醒消息(重要性级别-紧急：发出提示音，并以浮动通知的形式显示 & 锁屏显示 & 振动0.25s )
     */
    private val MENTION = NotificationCompatUtil.Channel(
        channelId = "MENTION",
        name = BaseApplication.getContext().getString(R.string.channel_mention),
        importance = NotificationManagerCompat.IMPORTANCE_HIGH,
        lockScreenVisibility = NotificationCompat.VISIBILITY_PUBLIC,
        vibrate = longArrayOf(0, 250)
    )

    /**
     * 通知渠道-系统通知(重要性级别-中：无提示音)
     */
    private val NOTICE = NotificationCompatUtil.Channel(
        channelId = "NOTICE",
        name = BaseApplication.getContext().getString(R.string.channel_notice),
        importance = NotificationManagerCompat.IMPORTANCE_LOW
    )

    /** 通知渠道-音视频通话(重要性级别-紧急：发出提示音，并以浮动通知的形式显示 & 锁屏显示 & 振动4s停2s再振动4s ) */
    private val CALL = NotificationCompatUtil.Channel(
        channelId = "CALL",
        name = BaseApplication.getContext().getString(R.string.channel_call),
        importance = NotificationManagerCompat.IMPORTANCE_HIGH,
        lockScreenVisibility = NotificationCompat.VISIBILITY_PUBLIC,
        vibrate = longArrayOf(0, 4000, 2000, 4000),
        sound = Uri.parse("android.resource://" + BaseApplication.getContext().packageName + "/" + R.raw.iphone)
    )

    /**
     * 显示聊天信息
     *
     * [context]    上下文
     * [id]         通知的唯一ID
     * [title]      标题
     * [text]       正文文本
     */
    fun notifyMessage(
        context: Context,
        id: Int,
        title: String?,
        text: String?
    ) {
        val intent = Intent(context, MainActivity::class.java)

        val builder =
            NotificationCompatUtil.createNotificationBuilder(context, MESSAGE, title, text, intent)

        // 默认情况下，通知的文字内容会被截断以放在一行。如果您想要更长的通知，可以使用 setStyle() 添加样式模板来启用可展开的通知。
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(text))

        NotificationCompatUtil.notify(context, id, buildDefaultConfig(builder))
    }

    /**
     * 显示@提醒消息
     *
     * [context]    上下文
     * [id]         通知的唯一ID
     * [title]      标题
     * [text]       正文文本
     */
    fun notifyMention(
        context: Context,
        id: Int,
        title: String?,
        text: String?
    ) {
        val intent = Intent(context, MainActivity::class.java)

        val builder = NotificationCompatUtil.createNotificationBuilder(
            context,
            MENTION,
            title,
            text,
            intent
        )

        // 默认情况下，通知的文字内容会被截断以放在一行。如果您想要更长的通知，可以使用 setStyle() 添加样式模板来启用可展开的通知。
        builder.setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(text)
        )

        NotificationCompatUtil.notify(context, id, buildDefaultConfig(builder));
    }

    /**
     * 显示系统通知
     *
     * [context]    上下文
     * [id]         通知的唯一ID
     * [title]      标题
     * [text]       正文文本
     */
    fun notifyNotice(
        context: Context,
        id: Int,
        title: String?,
        text: String?
    ) {
        val intent = Intent(context, MainActivity::class.java)

        val builder = NotificationCompatUtil.createNotificationBuilder(
            context,
            NOTICE,
            title,
            text,
            intent
        )

        NotificationCompatUtil.notify(context, id, buildDefaultConfig(builder));
    }

    /**
     * 显示音视频通话
     *
     * [context]    上下文
     * [id]         通知的唯一ID
     * [title]      标题
     * [text]       正文文本
     */
    fun notifyCall(
        context: Context,
        id: Int,
        title: String?,
        text: String?
    ) {
        val intent = Intent(context, MainActivity::class.java)

        val builder = NotificationCompatUtil.createNotificationBuilder(
            context,
            CALL,
            title,
            text,
            intent
        )

        NotificationCompatUtil.notify(context, id, buildDefaultConfig(builder));
    }

    /**
     * 构建应用通知的默认配置
     * [builder] 构建器
     */
    private fun buildDefaultConfig(builder: NotificationCompat.Builder): Notification {
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        return builder.build()
    }
}