package com.lomeone.texhol.config

sealed class DataSourceProperties(
    open val host: String,
    open val port: Int,
    open val credentials: Credentials
) {
    data class Relational(
        override val host: String,
        override val port: Int,
        override val credentials: Credentials,
        val databaseName: String,
        val driverClassName: String,
        val connectionTimeout: Long = 30000,
        val maxPoolSize: Int = 10
    ) : DataSourceProperties(host, port, credentials)

    data class Credentials(
        val username: String,
        val password: String
    )
}
