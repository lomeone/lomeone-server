package com.lomeone.application.graphql

import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [DgsAutoConfiguration::class, HelloDataFetcher::class])
class HelloDataFetcherTest(
    private val dgsQueryExecutor: DgsQueryExecutor
) : FreeSpec({
    "DataFetcher Query test Example" - {
        val result: String = dgsQueryExecutor.executeAndExtractJsonPath("""
                {
                    hello(input: {
                        helloName: "name"
                    })
                }
            """.trimIndent(), "data.hello")

        result shouldBe "name hello"
    }
})
