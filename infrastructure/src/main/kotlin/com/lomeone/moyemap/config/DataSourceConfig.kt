package com.lomeone.moyemap.config

import com.lomeone.eunoia.security.secret.SecretRegistry
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.hibernate.boot.model.naming.PhysicalNamingStrategySnakeCaseImpl
import org.hibernate.cfg.AvailableSettings
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.hibernate.SpringImplicitNamingStrategy
import org.springframework.boot.jpa.EntityManagerFactoryBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.hibernate.SpringBeanContainer
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.sql.DataSource

@Configuration("moyemapDataSourceConfig")
@ConfigurationProperties(prefix = "moyemap.database.datasource")
@EnableJpaRepositories(
    basePackages = ["com.lomeone.moyemap"],
    entityManagerFactoryRef = "moyemapEntityManagerFactory",
    transactionManagerRef = "moyemapTransactionManager"
)
class DataSourceConfig {
    lateinit var primary: DataSourceConfigProperties
    lateinit var readOnly: DataSourceConfigProperties

    private val logger = KotlinLogging.logger {}

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
    fun moyemapPrimaryDatasourceProperties(secretRegistry: SecretRegistry): DataSourceProperties.Relational {
        logger.debug { "Moyemap Primary DataSource Config - Host: ${primary.hostname}, Port: ${primary.port}, SecretId: ${primary.secretId}" }
        val secret = runBlocking { secretRegistry.getSecret(primary.secretId, DataSourceSecret::class) }
        logger.debug { "Moyemap Primary DataSource Secret - Username: ${secret.username}, Password: ${"*".repeat(secret.password.length)}" }

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
    fun moyemapPrimaryDataSource(moyemapPrimaryDatasourceProperties: DataSourceProperties.Relational): DataSource =
        createHikariDataSource(moyemapPrimaryDatasourceProperties)

    @Bean
    fun moyemapReadOnlyDatasourceProperties(secretRegistry: SecretRegistry): DataSourceProperties.Relational {
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
    fun moyemapReadOnlyDataSource(moyemapReadOnlyDatasourceProperties: DataSourceProperties.Relational): DataSource =
        createHikariDataSource(moyemapReadOnlyDatasourceProperties)

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
    fun moyemapRoutingDataSource(
        @Qualifier("moyemapPrimaryDataSource") moyemapPrimaryDataSource: DataSource,
        @Qualifier("moyemapReadOnlyDataSource") moyemapReadOnlyDataSource: DataSource
    ): DataSource {
        val dataSourceMap = mapOf<Any, Any>(
            DataSourceType.PRIMARY to moyemapPrimaryDataSource,
            DataSourceType.READ_ONLY to moyemapReadOnlyDataSource
        )

        return LazyConnectionDataSourceProxy(
            MoyemapReplicationRoutingDataSource().apply {
                setDefaultTargetDataSource(moyemapPrimaryDataSource)
                setTargetDataSources(dataSourceMap)
                afterPropertiesSet()
            }
        )
    }

    @Bean
    fun moyemapEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("moyemapRoutingDataSource") moyemapRoutingDataSource: DataSource,
        beanFactory: ConfigurableListableBeanFactory
    ): LocalContainerEntityManagerFactoryBean =
        builder
            .dataSource(moyemapRoutingDataSource)
            .packages("com.lomeone.moyemap", "com.lomeone.common")
            .persistenceUnit("moyemap")
            .properties(jpaProperties(beanFactory))
            .build().apply {
                setEntityManagerFactoryInterface(jakarta.persistence.EntityManagerFactory::class.java)
            }

    @Bean
    fun moyemapTransactionManager(moyemapEntityManagerFactory: LocalContainerEntityManagerFactoryBean): PlatformTransactionManager =
        JpaTransactionManager(moyemapEntityManagerFactory.`object`!!)

    private fun jpaProperties(beanFactory: ConfigurableListableBeanFactory): Map<String, Any> =
        mutableMapOf<String, Any>(
            "hibernate.physical_naming_strategy" to PhysicalNamingStrategySnakeCaseImpl::class.java.name,
            "hibernate.implicit_naming_strategy" to SpringImplicitNamingStrategy::class.java.name,
            "hibernate.hbm2ddl.auto" to "validate",
            AvailableSettings.BEAN_CONTAINER to SpringBeanContainer(beanFactory)
        )
}

enum class DataSourceType {
    PRIMARY, READ_ONLY
}

class MoyemapReplicationRoutingDataSource : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any =
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) DataSourceType.READ_ONLY else DataSourceType.PRIMARY
}
