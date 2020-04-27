package com.wendy.imagepickerdemo.main.di


object Injector {

    val maps: MutableMap<Class<*>, Any> = mutableMapOf()

    inline fun <reified T> get(): T {
        return maps[T::class.java] as T
    }

    inline fun <reified T> provide(): T {
        try {
            return T::class.java.newInstance()
        } catch (e: Exception) {
            throw IllegalStateException(
                "Cannot provide instance of null parameters in Constructor",
                e
            )
        }
    }

    fun store(clazz: Class<*>, obj: Any) {
        maps[clazz] = obj
    }

    fun remove(clazz: Class<*>) {
        maps.remove(clazz)
    }
}