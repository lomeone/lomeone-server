package com.lomeone.config.datasource.dynamodb

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.instrumentation.awssdk.v2_2.AwsSdkTelemetry
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

@Configuration
@ConfigurationProperties(prefix = "cloud.aws.dynamodb")
class DynamoConfig {
    lateinit var region: String
    lateinit var host: String

    @Bean
    fun dynamoDbClient(openTelemetry: OpenTelemetry): DynamoDbClient {
        val awsSdkTelemetry = AwsSdkTelemetry.create(openTelemetry)

        return DynamoDbClient.builder()
            .region(Region.of(region))
            .endpointOverride(URI.create(host))
            .overrideConfiguration(
                ClientOverrideConfiguration.builder()
                    .addExecutionInterceptor(awsSdkTelemetry.createExecutionInterceptor())
                    .build()
            ).build()
    }

    @Bean
    fun dynamoDbEnhancedAsyncClient(dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient {
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build()
    }
}
