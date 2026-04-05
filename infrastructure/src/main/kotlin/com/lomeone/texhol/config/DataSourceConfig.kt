package com.lomeone.texhol.config

import com.lomeone.eunoia.security.secret.SecretRegistry
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.hibernate.boot.model.naming.PhysicalNamingStrategySnakeCaseImpl
import org.hibernate.cfg.AvailableSettings
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.hibernate.SpringImplicitNamingStrategy
import org.springframework.boot.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.hibernate.SpringBeanContainer
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.sql.DataSource

enum class DataSourceType {
    PRIMARY, READ_ONLY
}

@Configuration
@ConfigurationProperties(prefix = "texhol.database.datasource")
@EnableJpaAuditing
class DataSourceConfig {
    lateinit var primary: DataSourceConfigProperties
    lateinit var readOnly: DataSourceConfigProperties

    data class DataSourceConfigProperties(
        val secretId: String,
        val hostname: String,
        val port: String,
        val databaseName: String,
        val driverClassName: String,
        val connectionTimeout: Long = 30000,
        val maxPoolSize: Int = 10
    )

    @Serializable
    data class DataSourceSecret(
        val username: String,
        val password: String
    )

    @Bean
    fun primaryDatasourceProperties(secretRegistry: SecretRegistry): DataSourceProperties.Relational {
        println("Primary DataSource Config - Host: ${primary.hostname}, Port: ${primary.port}, SecretId: ${primary.secretId}")
        val secret = runBlocking { secretRegistry.getSecret(primary.secretId, DataSourceSecret::class) }
        println("Primary DataSource Secret - Username: ${secret.username}, Password: ${"*".repeat(secret.password.length)}")

        return DataSourceProperties.Relational(
            host = primary.hostname,
            port = primary.port.toInt(),
            credentials = DataSourceProperties.Credentials(
                username = secret.username,
                password = secret.password
            ),
            databaseName = primary.databaseName,
            driverClassName = primary.driverClassName,
            connectionTimeout = primary.connectionTimeout,
            maxPoolSize = primary.maxPoolSize
        )
    }

    @Bean
    fun primaryDataSource(primaryDatasourceProperties: DataSourceProperties.Relational): DataSource = createHikariDataSource(primaryDatasourceProperties)

    @Bean
    fun readOnlyDatasourceProperties(secretRegistry: SecretRegistry): DataSourceProperties.Relational {
        val secret = runBlocking { secretRegistry.getSecret(readOnly.secretId, DataSourceSecret::class) }

        return DataSourceProperties.Relational(
            host = readOnly.hostname,
            port = readOnly.port.toInt(),
            credentials = DataSourceProperties.Credentials(
                username = secret.username,
                password = secret.password
            ),
            databaseName = readOnly.databaseName,
            driverClassName = readOnly.driverClassName,
            connectionTimeout = readOnly.connectionTimeout,
            maxPoolSize = readOnly.maxPoolSize
        )
    }

    @Bean
    fun readOnlyDataSource(readOnlyDatasourceProperties: DataSourceProperties.Relational): DataSource = createHikariDataSource(readOnlyDatasourceProperties)


    private fun createHikariDataSource(properties: DataSourceProperties.Relational): DataSource =
        HikariDataSource(
            HikariConfig().apply {
                jdbcUrl = "jdbc:mysql://${properties.host}:${properties.port}/${properties.databaseName}"
                username = properties.credentials.username
                password = properties.credentials.password
                driverClassName = properties.driverClassName
                connectionTimeout = properties.connectionTimeout
                maximumPoolSize = properties.maxPoolSize
            }
        )

    @Bean
    @Primary
    fun routingDataSource(
        primaryDataSource: DataSource,
        readOnlyDataSource: DataSource
    ): DataSource {
        val dataSourceMap = mapOf<Any, Any>(
            DataSourceType.PRIMARY to primaryDataSource,
            DataSourceType.READ_ONLY to readOnlyDataSource
        )

        return LazyConnectionDataSourceProxy(
            ReplicationRoutingDataSource().apply {
                setDefaultTargetDataSource(primaryDataSource)
                setTargetDataSources(dataSourceMap)
                afterPropertiesSet()
            }
        )
    }

    @Bean
    @Primary
    fun entityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        routingDataSource: DataSource,
        beanFactory: ConfigurableListableBeanFactory
    ): LocalContainerEntityManagerFactoryBean =
        builder
            .dataSource(routingDataSource)
            .packages("com.oliveyoung.global") // 비즈니스 엔티티 위치
            .persistenceUnit("business")
            .properties(jpaProperties(beanFactory))
            .build().apply {
                setEntityManagerFactoryInterface(jakarta.persistence.EntityManagerFactory::class.java)
            }

    private fun jpaProperties(beanFactory: ConfigurableListableBeanFactory): Map<String, Any> =
        mutableMapOf<String, Any>(
            "hibernate.physical_naming_strategy" to PhysicalNamingStrategySnakeCaseImpl::class.java.name,
            "hibernate.implicit_naming_strategy" to SpringImplicitNamingStrategy::class.java.name,
            "hibernate.hbm2ddl.auto" to "validate",
            AvailableSettings.BEAN_CONTAINER to SpringBeanContainer(beanFactory)
        )
}

class ReplicationRoutingDataSource : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any {
        return if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) DataSourceType.READ_ONLY else DataSourceType.PRIMARY
    }
}
