package com.yjn.eventbus

import java.lang.reflect.Method

/**
 * <pre>
 *     author: Bruce_Yang
 *     email : yangjianan@seuic.com
 *     time  : 2020/4/10
 *     desc  : 接收消息的方法
 * </pre>
 */
class SubscribeMethod(
    //注册方法
    var method: Method,
    //线程类型（枚举类）
    var threadModel: ThreadModel,
    //参数类型(接收事件对象)
    var eventType: Class<*>
)

