package com.yjn.eventbus

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * <pre>
 *     author: Bruce_Yang
 *     email : yangjianan@seuic.com
 *     time  : 2020/4/10
 *     desc  : EventBus单例
 * </pre>
 */
object EventBus {
    // 所有注册的缓存
    private val cacheMap: MutableMap<Any, List<SubscribeMethod>> = HashMap()
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var executorService: ExecutorService = Executors.newCachedThreadPool()

    /**
     * 注册
     * @param subscriber 注册的Activity实例
     */
    fun register(subscriber: Any) {
        //注册缓存中不存在该Activity实例
        if (!cacheMap.containsKey(subscriber)) {
            cacheMap[subscriber] = getSubscribeList(subscriber)
        }
    }

    /**
     * 获取当前activity中所有注册的订阅方法
     * @param subscriber 注册的Activity实例
     */
    private fun getSubscribeList(subscriber: Any): List<SubscribeMethod> {
        val list: MutableList<SubscribeMethod> = arrayListOf()

        var javaClass = subscriber.javaClass

        while (javaClass != null) {
            //判断分类是在那个包下，（如果是系统的就不需要）
            val name = javaClass.name
            if (name.startsWith("java.") ||
                name.startsWith("javax.") ||
                name.startsWith("android.") ||
                name.startsWith("androidx.")
            ) {
                break
            }
            //当前activity中所有的方法
            val declaredMethods = javaClass.declaredMethods
            declaredMethods.forEach {
                //如果没有Subscribe注解的方法，就返回
                val annotation = it.getAnnotation(Subscribe::class.java) ?: return@forEach
                //检测参数个数是否符合
                val parameterTypes = it.parameterTypes
                if (parameterTypes.size != 1) {
                    throw RuntimeException("EventBus只能接收一个参数")
                }
                //符合
                val threadModel = annotation.threadModel
                //构造订阅方法的类对象
                val subscribeMethod = SubscribeMethod(it, threadModel, parameterTypes[0])
                list.add(subscribeMethod)
            }
            javaClass = javaClass.superclass as Class<Any>
        }

        return list
    }

    /**
     * 取消注册
     * @param subscriber 注册的Activity实例
     */
    fun unRegister(subscriber: Any) {
        if (cacheMap.containsKey(subscriber)) {
            cacheMap.remove(subscriber)
        }
    }

    /**
     * 发送消息
     * @param obj 承载消息的消息对象
     */
    fun post(obj: Any) {
        for ((subscribe, subscribeMethodList) in cacheMap) {
            subscribeMethodList.forEach {
                //判断注解的方法是否应该接收事件
                if (it.eventType.isAssignableFrom(obj::class.java)) {
                    when (it.threadModel) {
                        ThreadModel.POSTING -> {
                            //默认情况，不进行线程切换，post方法是什么线程，接收方法就是什么线程
                            invoke(it, subscribe, obj)
                        }
                        // 接收方法在主线程执行的情况
                        ThreadModel.MAIN, ThreadModel.MAIN_ORDERED -> {
                            // Post方法在主线程执行的情况
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                invoke(it, subscribe, obj)
                            } else {
                                // 在子线程中接收，主线程中接收消息
                                handler.post { invoke(it, subscribe, obj) }
                            }
                        }
                        //接收方法在子线程的情况
                        ThreadModel.ASYNC -> {
                            //Post方法在主线程的情况
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                executorService.execute { invoke(it, subscribe, obj) }
                            } else {
                                //Post方法在子线程的情况
                                invoke(it, subscribe, obj)
                            }
                        }

                    }

                }
            }
        }
    }

    /**
     * 执行接收消息方法
     * @param subscribeMethod 需要接收消息的方法
     * @param subscribe Activity注册类
     * @param obj 接收的参数（即post的参数对象）
     */
    private fun invoke(subscribeMethod: SubscribeMethod, subscribe: Any, obj: Any) {
        subscribeMethod.method
            .invoke(subscribe, obj)
    }
}