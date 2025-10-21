package dev.vms.wcb.googlegenai.rag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

//@Service
public class DocumentIngestionService {

    private final DocumentTransformerFactory transformerFactory;
    private final VectorStore vectorStore;

    public DocumentIngestionService(DocumentTransformerFactory transformerFactory,
                                    VectorStore vectorStore) {
        this.transformerFactory = transformerFactory;
        this.vectorStore = vectorStore;
    }

    public void ingestFile(File file) throws IOException {
        String text = transformerFactory.transform(file);
        List<Document> chunks = splitIntoChunks(text, 800);  // ~800 tokens per chunk
        for (Document doc : chunks) {
            doc.getMetadata().put("filename", file.getName());
        }
        vectorStore.add(chunks);
        System.out.println("✅ Ingested: " + file.getName());        
    }

    private List<Document> splitIntoChunks(String text, int chunkSize) {
        List<Document> docs = new ArrayList<>();
        int length = text.length();
        for (int start = 0; start < length; start += chunkSize) {
            int end = Math.min(length, start + chunkSize);
            String chunk = text.substring(start, end);
            docs.add(new Document(chunk, Map.of()));
        }
        return docs;
    }
}
