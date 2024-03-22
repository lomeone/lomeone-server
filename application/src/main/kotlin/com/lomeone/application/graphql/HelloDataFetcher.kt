package com.lomeone.application.graphql

import com.lomeone.generated.types.HelloInput
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class HelloDataFetcher {
    @DgsQuery
    fun helloQuery(@InputArgument input: HelloInput): String {
        return "query success ${input.helloName} hello"
    }

    @DgsMutation
    fun helloMutation(@InputArgument input: HelloInput): String {
        return "mutation success ${input.helloName} hello"
    }
}
