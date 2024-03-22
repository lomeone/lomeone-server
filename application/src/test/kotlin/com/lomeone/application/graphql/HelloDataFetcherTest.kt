package com.lomeone.application.graphql

import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.netflix.graphql.dgs.autoconfig.DgsExtendedScalarsAutoConfiguration
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [
    DgsAutoConfiguration::class,
    DgsExtendedScalarsAutoConfiguration::class,
    HelloDataFetcher::class
])
class HelloDataFetcherTest(
    private val dgsQueryExecutor: DgsQueryExecutor,
    private val dgsMutationExecutor: DgsQueryExecutor
) : FreeSpec({
    "DataFetcher Query test Example" - {
        val result: String = dgsQueryExecutor.executeAndExtractJsonPath("""
                query {
                    helloQuery(input: {
                        helloName: "name"
                    })
                }
            """.trimIndent(), "data.helloQuery")

        result shouldBe "query success name hello"
    }

    "DataFetcher Mutation test Example" - {
        val result: String = dgsMutationExecutor.executeAndExtractJsonPath("""
            mutation {
                helloMutation(input: {
                    helloName: "name"
                })
            }
        """.trimIndent(), "data.helloMutation")

        result shouldBe "mutation success name hello"
    }
})
