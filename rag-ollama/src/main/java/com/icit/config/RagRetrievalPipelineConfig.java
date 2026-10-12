package com.icit.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagRetrievalPipelineConfig {
    /**
     * Builds the retrieval pipeline used by the assistant.
     *
     * <p>There are two {@code EmbeddingStore<TextSegment>} beans, so the
     * {@code @Qualifier} annotations tell Spring exactly which one belongs to each
     * knowledge base. Without qualifiers, injection by type would be ambiguous.</p>
     *
     * @param babyProductEmbeddingStore store searched for baby-product information
     * @param vaccineEmbeddingStore store searched for vaccine information
     * @param embeddingModel model used to embed each incoming query before vector search
     * @return a retrieval augmentor that searches both knowledge bases
     */
    @Bean
    public RetrievalAugmentor ragRetrievalAugmentor(@Qualifier("babyProductEmbeddingStore") EmbeddingStore<TextSegment> babyProductEmbeddingStore,
                                                    @Qualifier("vaccineEmbeddingStore") EmbeddingStore<TextSegment> vaccineEmbeddingStore,
                                                    EmbeddingModel embeddingModel) {

     // A retriever turns a user's text query into a vector, searches its
     // associated store for similar vectors, and returns the matching segments.
     ContentRetriever babyRetriever = EmbeddingStoreContentRetriever.builder()
     // Use the same embedding model used when documents were ingested.
     .embeddingModel(embeddingModel)
     .embeddingStore(babyProductEmbeddingStore)
     // Return at most the three best matches from this store.
     .maxResults(3)
     // Discard matches whose retrieval score is below this threshold.
     // Tune this against real queries and the store's score behavior.
     .minScore(0.5)
     .build();

     ContentRetriever vaccineRetriever = EmbeddingStoreContentRetriever.builder()
     // Use the same model so vaccine searches produce vectors that can
     // be compared with the vectors stored during vaccine ingestion.
     .embeddingModel(embeddingModel)
     .embeddingStore(vaccineEmbeddingStore)
     // Apply the same result count and score cutoff as the other
     // knowledge base; tune these values using representative queries.
     .maxResults(3)
     .minScore(0.5)
     .build();

     // DefaultQueryRouter sends every query to every configured retriever.
     // It does not classify a query as "baby products" or "vaccines" first.
     // The default retrieval augmentor combines the content returned by them.
     QueryRouter queryRouter = new DefaultQueryRouter(babyRetriever,vaccineRetriever);

     // This is the object supplied to the assistant/AI service. It coordinates
     // query routing, retrieval, and adding the retrieved context to the prompt
     // before the chat model generates its answer.
     return DefaultRetrievalAugmentor.builder()
     .queryRouter(queryRouter)
     .build();
     }
}
