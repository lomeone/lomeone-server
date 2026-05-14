package com.lomeone.config.aws

import aws.sdk.kotlin.services.secretsmanager.SecretsManagerClient
import com.lomeone.eunoia.aws.security.SecretsManager
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("cloud.aws")
class SecurityConfiguration {
    lateinit var region: String
    @Bean
    fun secretsMangerClient() = SecretsManagerClient { region = this@SecurityConfiguration.region }

    @Bean
    fun secretRegistry(secretsManagerClient: SecretsManagerClient) = SecretsManager(secretsManagerClient)
}
