package dev.vms.wcb.googlegenai.rag;

import java.io.File;
import java.io.IOException;

public interface DocumentTransformer {
    boolean supports(File file);
    String transform(File file) throws IOException;
}
