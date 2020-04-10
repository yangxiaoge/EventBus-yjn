package com.seuic.weight.eventbus_my

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yjn.eventbus.EventBus
import kotlinx.android.synthetic.main.activity_second.*
import kotlin.concurrent.thread

/**
 * <pre>
 *     author: Bruce_Yang
 *     email : yangjianan@seuic.com
 *     time  : 2020/4/10
 *     desc  : 第二个页面
 * </pre>
 */
class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        send_msg_btn.setOnClickListener {
            EventBus.post(MsgEvent("来自第二个页面的消息：${System.currentTimeMillis()}"))
            finish()
        }

        send_msg_asyn_btn.setOnClickListener {
            thread {
                EventBus.post(MsgEvent("来自子线程的消息"))
                finish()
            }
        }
    }
}
