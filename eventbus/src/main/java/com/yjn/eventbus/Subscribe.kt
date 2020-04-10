package com.yjn.eventbus

/**
 * <pre>
 *     author: Bruce_Yang
 *     email : yangjianan@seuic.com
 *     time  : 2020/4/10
 *     desc  : EventBus接收方法注解
 * </pre>
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Subscribe(val threadModel: ThreadModel = ThreadModel.POSTING)
