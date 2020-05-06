package dev.wendyyanto.imagepicker.di


object Injector {

    val maps: MutableMap<Class<*>, Any> = mutableMapOf()

    inline fun <reified T> get(): T {
        return maps[T::class.java] as T
    }

    fun store(clazz: Class<*>, obj: Any) {
        maps[clazz] = obj
    }

    fun remove(clazz: Class<*>) {
        maps.remove(clazz)
    }
}