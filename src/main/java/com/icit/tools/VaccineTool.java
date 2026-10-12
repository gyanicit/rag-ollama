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

@Component("vaccineTool")
public class VaccineTool {

    private final EmbeddingStore<TextSegment> vaccineEmbeddingStore;
    private final EmbeddingModel embeddingModel;

    public VaccineTool(@Qualifier("vaccineEmbeddingStore") EmbeddingStore<TextSegment> vaccineEmbeddingStore, EmbeddingModel embeddingModel) {
        this.vaccineEmbeddingStore = vaccineEmbeddingStore;
        this.embeddingModel = embeddingModel;
    }


    @Tool("Search vaccine information by name or disease")
    public String searchVaccine(String query){
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        EmbeddingSearchResult<TextSegment> searchResult= vaccineEmbeddingStore.search(EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(3)
                .minScore(0.5)
                .build());

        return searchResult.matches().stream().map(m-> m.embedded().text())
                .collect(Collectors.joining("\n\n"));

    }

    @Tool("Get vaccine scheduled by age")
    public String getVaccineScheduledByAge(String query){
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        EmbeddingSearchResult<TextSegment> searchResult= vaccineEmbeddingStore.search(EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(3)
                .minScore(0.5)
                .build());

        return searchResult.matches().stream().map(m-> m.embedded().text())
                .collect(Collectors.joining("\n\n"));

    }

}
