package org.oleg.iem.services.lmm

import io.qdrant.client.QdrantClient
import io.qdrant.client.QdrantGrpcClient
import io.qdrant.client.grpc.Collections
import io.qdrant.client.grpc.Points
import io.qdrant.client.grpc.Points.PointStruct

class VectorDatabaseClient(val qdrantHost: String, qdrantPort: Int, private val embeddingDimensionality: Int) {
    private val qdrantClient: QdrantClient = QdrantClient(
        QdrantGrpcClient.newBuilder(qdrantHost,
            qdrantPort,
            false).build()
    )

    fun createCollectionIfNotExist(collectionName: String){
        if (!qdrantClient.collectionExistsAsync(collectionName).get()){
            qdrantClient.createCollectionAsync(collectionName,
                Collections.VectorParams.newBuilder().setDistance(Collections.Distance.Dot).setSize(
                    embeddingDimensionality + 0L).build()).get()
        }
    }

    fun upsertVectorDatabase(vectorData: ArrayList<PointStruct>, collectionName: String){
        val operationInfo: Points.UpdateResult = qdrantClient.upsertAsync(collectionName, vectorData).get()
        // println(operationInfo)
    }

    fun queryVectorDatabase(request: Points.QueryPoints): List<Points.ScoredPoint>{
        return qdrantClient.queryAsync(request).get()
    }
}