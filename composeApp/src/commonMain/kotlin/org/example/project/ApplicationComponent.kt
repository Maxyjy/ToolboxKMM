package org.example.project

import org.example.project.util.CoreComponent
import org.example.project.util.CoreComponentImpl

/**
 *
 *
 * @author YangJianyu
 * @date 2024/9/23
 */
object ApplicationComponent {
    private var _coreComponent: CoreComponent? = null
    val coreComponent
        get() = _coreComponent
            ?: throw IllegalStateException("Make sure to call ApplicationComponent.init()")

    fun init() {
        _coreComponent = CoreComponentImpl()
    }
}

val coreComponent get() = ApplicationComponent.coreComponent