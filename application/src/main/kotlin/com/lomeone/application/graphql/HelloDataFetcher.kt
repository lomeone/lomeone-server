package com.lomeone.application.graphql

import com.lomeone.generated.types.HelloInput
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class HelloDataFetcher {
    @DgsQuery
    fun hello(@InputArgument input: HelloInput): String {
        return "${input.helloName} hello"
    }
}
