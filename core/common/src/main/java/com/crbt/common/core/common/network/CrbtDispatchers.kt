
package com.crbt.common.core.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val crbtDispatchers: CrbtDispatchers)

enum class CrbtDispatchers {
    Default,
    IO,
}
