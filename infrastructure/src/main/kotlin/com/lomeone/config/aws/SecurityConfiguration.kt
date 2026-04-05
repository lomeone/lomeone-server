package com.lomeone.config.aws

import aws.sdk.kotlin.services.secretsmanager.SecretsManagerClient
import com.lomeone.eunoia.aws.security.SecretsManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SecurityConfiguration {
    @Bean
    fun secretsMangerClient() = SecretsManagerClient { region = "ap-northeast-2" }

    @Bean
    fun secretRegistry(secretsManagerClient: SecretsManagerClient) = SecretsManager(secretsManagerClient)
}
