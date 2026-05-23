package com.lomeone.moyemap.common

import org.springframework.core.annotation.AliasFor
import org.springframework.transaction.annotation.Transactional

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Transactional("moyemapTransactionManager")
annotation class MoyemapTransactional(
    @get:AliasFor(annotation = Transactional::class, attribute = "readOnly")
    val readOnly: Boolean = false
)
