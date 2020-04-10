package com.yjn.eventbus

/**
 * <pre>
 *     author: Bruce_Yang
 *     email : yangjianan@seuic.com
 *     time  : 2020/4/10
 *     desc  : 线程枚举类型
 * </pre>
 */
enum class ThreadModel {
    // 默认模式，无论post是在子线程或主线程，接收方法的线程为post时的线程。
    // 不进行线程切换
    POSTING,

    // 主线程模式，无论post是在子线程或主线程，接收方法的线程都切换为主线程。
    MAIN,

    // 主线程模式，无论post是在子线程或主线程，接收方法的线程都切换为主线程。
    // 这个在EventBus源码中与MAIN不同， 事件将一直排队等待交付。这确保了post调用是非阻塞的。
    // 此处不做其他处理，直接按照主线程模式处理
    MAIN_ORDERED,

    // 子线程模式，无论post是在子线程或主线程，接收方法的线程都切换为子线程。
    ASYNC
}
