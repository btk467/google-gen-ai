package dev.vms.wcb.googlegenai.rag;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DocumentTransformerFactory {

    private final List<DocumentTransformer> transformers;

    public DocumentTransformerFactory(List<DocumentTransformer> transformers) {
        this.transformers = transformers;
    }

    public String transform(File file) throws IOException {
        for (DocumentTransformer transformer : transformers) {
            if (transformer.supports(file)) {
                return transformer.transform(file);
            }
        }
        throw new IllegalArgumentException("Unsupported file type: " + file.getName());
    }
}
