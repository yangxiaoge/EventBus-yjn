package com.seuic.weight.eventbus_my

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.yjn.eventbus.EventBus
import com.yjn.eventbus.Subscribe
import com.yjn.eventbus.ThreadModel
import kotlinx.android.synthetic.main.activity_main.*

/**
 * <pre>
 *     author: Bruce_Yang
 *     email : yangjianan@seuic.com
 *     time  : 2020/4/10
 *     desc  : 首页
 * </pre>
 */
class MainActivity : AppCompatActivity() {
    companion object{
        val tag = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.register(this)

        initViews()
    }

    private fun initViews() {
        go2second_btn.setOnClickListener {
            startActivity(Intent(this,SecondActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.unRegister(this)
    }

    @Subscribe(ThreadModel.MAIN)
    fun getMsgEvent(msgEvent: MsgEvent){
        msg_tv.text = msgEvent.msg
        Toast.makeText(this,msgEvent.msg,Toast.LENGTH_SHORT).show()
        Log.d(tag,"msgEvent = ${msgEvent.msg}")
    }

    @Subscribe(ThreadModel.ASYNC)
    fun getMsgEvent222(msgEvent: MsgEvent){
        Log.d(tag,"msgEvent222 = ${msgEvent.msg}")
    }
}
