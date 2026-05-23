package com.lomeone.texhol.common

import org.springframework.core.annotation.AliasFor
import org.springframework.transaction.annotation.Transactional

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Transactional("texholTransactionManager")
annotation class TexholTransactional(
    @get:AliasFor(annotation = Transactional::class, attribute = "readOnly")
    val readOnly: Boolean = false
)
