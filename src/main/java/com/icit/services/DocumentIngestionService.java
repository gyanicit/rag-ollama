package com.icit.services;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.PostConstruct;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

@Service
public class DocumentIngestionService {


    private static final Logger logger= LoggerFactory.getLogger(DocumentIngestionService.class);

    @Autowired
    @Qualifier("babyProductEmbeddingStore")
    private EmbeddingStore<TextSegment> babyProductEmbeddingStore;
    @Autowired
    @Qualifier("vaccineEmbeddingStore")
    private EmbeddingStore<TextSegment> vaccineEmbeddingStore;
    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void ingestAll(){
        ingestionPipeline("baby_products_catalog_100.pdf", babyProductEmbeddingStore);
        ingestionPipeline("vaccine_dummy_catalog_200.pdf", vaccineEmbeddingStore);
    }

    private void ingestionPipeline(String filename, EmbeddingStore<TextSegment> store) {
        try {
            if(isAlreadyIngested(filename)){
                logger.error("Document already ingested for file: {}", filename);
                return;
            }
            URL resource = this.getClass().getClassLoader().getResource("docs/"+filename);
            if(resource == null){
                logger.error("Pdf {} not found", filename);
                return;
            }

            Path path = Paths.get(resource.toURI());
            //Document loading
            Document document = FileSystemDocumentLoader.loadDocument(path, new ApachePdfBoxDocumentParser());
            Map<String, Object> metadata = createMetadata(filename, path);
            document.metadata().putAll(metadata);
            //Ingestor with the help of
            EmbeddingStoreIngestor ingestor = getIngestor(store);
            ingestor.ingest(document);
            trackIngestion(filename);
            logger.info("Ingested {}", filename);

        } catch (Exception exception) {
            logger.error("Exception occurred During document ingestion", exception);
        }
    }

    private @NonNull Map<String, Object> createMetadata(String filename, Path path) throws Exception {
        Map<String,Object> metadata=new HashMap<>();
        String versionHash = sha256(path); // hash the PDF bytes before embedding
        metadata.put("source_id", filename);
        metadata.put("version_hash", versionHash);
        metadata.put("file_name", filename);
        return metadata;
    }

    private EmbeddingStoreIngestor getIngestor(EmbeddingStore<TextSegment> store) {
        //Chunker/Splitter
        DocumentSplitter splitter = DocumentSplitters.recursive(500, 50);
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(splitter)
                //Embedding API (Embedding Model [Mistral])
                .embeddingModel(embeddingModel)
                .embeddingStore(store)
                .build();
    }

    private boolean isAlreadyIngested(String fileName){
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ingestion_log WHERE file_name = ?", Integer.class, fileName);
        return count != null && count>0;
    }

    private void trackIngestion(String fileName){
        jdbcTemplate.update("INSERT INTO ingestion_log (file_name) values (?)", fileName);
    }

    private String sha256(Path path) throws Exception {
        byte[] bytes = Files.readAllBytes(path);
        byte[] digest = MessageDigest.getInstance("SHA-256").digest(bytes);
        return HexFormat.of().formatHex(digest);
    }
}
