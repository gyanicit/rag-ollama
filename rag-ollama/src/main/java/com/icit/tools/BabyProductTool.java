package com.icit.tools;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("babyProductTool")
public class BabyProductTool {

    private final EmbeddingStore<TextSegment> babyProductEmbeddingStore;
    private final EmbeddingModel embeddingModel;

    public BabyProductTool(@Qualifier("babyProductEmbeddingStore") EmbeddingStore<TextSegment> babyProductEmbeddingStore, EmbeddingModel embeddingModel) {
        this.babyProductEmbeddingStore = babyProductEmbeddingStore;
        this.embeddingModel = embeddingModel;
    }

    @Tool("Search for baby product by name, age category, price. Use this for any product related information")
    public String searchProduct(String query){
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        EmbeddingSearchResult<TextSegment> searchResult= babyProductEmbeddingStore.search(EmbeddingSearchRequest.builder()
                        .queryEmbedding(queryEmbedding)
                        .maxResults(3)
                        .minScore(0.5)
                .build());

        return searchResult.matches().stream().map(m-> m.embedded().text())
                .collect(Collectors.joining("\n\n"));
    }

    @Tool("Get products currently in stock")
    public String getInStockProducts(String query){
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        EmbeddingSearchResult<TextSegment> searchResult= babyProductEmbeddingStore.search(EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(3)
                .minScore(0.5)
                .build());

        return searchResult.matches().stream().map(m-> m.embedded().text())
                .filter(p->p.equalsIgnoreCase("in stock"))
                .collect(Collectors.joining("\n\n"));
    }
}
