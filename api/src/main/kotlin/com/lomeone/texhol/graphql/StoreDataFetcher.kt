package com.lomeone.texhol.graphql

import com.lomeone.generated.types.CreateStoreInput
import com.lomeone.generated.types.CreateStorePayload
import com.lomeone.generated.types.Store
import com.lomeone.texhol.store.service.CreateStore
import com.lomeone.texhol.store.service.CreateStoreCommand
import com.lomeone.texhol.store.service.GetStore
import com.lomeone.texhol.store.service.GetStoreCommand
import com.lomeone.texhol.store.service.GetStores
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class StoreDataFetcher(
    private val createStore: CreateStore,
    private val getStore: GetStore,
    private val getStores: GetStores
) {
    @DgsQuery
    fun store(@InputArgument id: String): Store {
        val storeEntity = getStore(GetStoreCommand(storeId = id.toLong()))
        return storeEntity.toGraphQL()
    }

    @DgsQuery
    fun stores(): List<Store> {
        return getStores().map { it.toGraphQL() }
    }

    @DgsMutation
    fun createStore(@InputArgument input: CreateStoreInput): CreateStorePayload {
        val result = createStore(
            CreateStoreCommand(
                name = input.name,
                location = input.location,
                address = input.address,
                imageUrl = input.imageUrl
            )
        )
        val storeEntity = getStore(GetStoreCommand(storeId = result.id))
        return CreateStorePayload(store = storeEntity.toGraphQL())
    }

    private fun com.lomeone.texhol.store.entity.Store.toGraphQL() = Store(
        id = this.id.toString(),
        name = this.name,
        location = this.location,
        address = this.address,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )
}
