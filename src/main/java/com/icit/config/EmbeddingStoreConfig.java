package com.icit.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class EmbeddingStoreConfig {
    /**
     * Creates the pgvector store for baby product documents.
     *
     * <p>The {@code EmbeddingModel} argument is injected by Spring from the model
     * bean above. The store keeps each vector together with its text segment and
     * metadata, so a successful search can return source text to the RAG pipeline.</p>
     *
     * @param embeddingModel model whose vector size must match the PostgreSQL column
     * @return the baby-product embedding store
     */
    @Bean
    @Primary
    public EmbeddingStore<TextSegment> babyProductEmbeddingStore(EmbeddingModel embeddingModel) {
        return PgVectorEmbeddingStore.builder()
                // Connection details for the PostgreSQL instance running locally.
                // For another environment, externalize these settings instead of
                // changing and rebuilding the application.
                .host("localhost")
                .port(5433)
                .database("vectordb")
                .user("postgres")
                .password("postgres")
                // This table contains vectors for the baby-product knowledge base.
                .table("baby_product_embedding")
                //.metadataStorageConfig()
                // pgvector needs a fixed vector length. Read it from the model so
                // the table schema matches the vectors that ingestion and queries
                // will produce. Existing tables must also use this same dimension.
                .dimension(embeddingModel.dimension())
                // Create the table if it does not exist. This does not migrate an
                // already-existing table whose vector dimension is different.
                .createTable(true)
                .build();
    }

    /**
     * Creates a second pgvector store for vaccine documents. It uses the same
     * database connection and embedding model dimension, but a different table so
     * that the two knowledge bases remain separately searchable.
     *
     * @param embeddingModel model whose vector size must match the PostgreSQL column
     * @return the vaccine embedding store
     */
    @Bean
    public EmbeddingStore<TextSegment> vaccineEmbeddingStore(EmbeddingModel embeddingModel) {
        return PgVectorEmbeddingStore.builder()
                // These connection settings must point to the same database used
                // by the baby-product store.
                .host("localhost")
                .port(5433)
                .database("vectordb")
                .user("postgres")
                .password("postgres")
                // Separate table for the vaccine knowledge base.
                .table("vaccine_embedding")
                // Keep this dimension aligned with the embedding model and with
                // this table's existing schema, if the table was created earlier.
                .dimension(embeddingModel.dimension())
                .createTable(true)
                .build();
    }
}
