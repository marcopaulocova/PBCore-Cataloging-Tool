package digitalbedrock.software.pbcore.lucene;

import digitalbedrock.software.pbcore.core.PBcoreValidator;
import javafx.concurrent.Task;
import org.apache.lucene.index.IndexWriter;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

class LuceneIndexingTask extends Task<Boolean> {

    private final IndexWriter indexWriter;
    private final String folder;
    private final String file;

    public LuceneIndexingTask(IndexWriter indexWriter, String folder, String file) {
        this.folder = folder;
        this.file = file;
        this.indexWriter = indexWriter;
    }

    public String getFolder() {
        return folder;
    }

    public String getFile() {
        return file;
    }

    @Override
    protected Boolean call() {
        if (isCancelled()) {
            return null;
        }
        try {
            File f = new File(file);
            new PBcoreValidator().validate(f);
            LuceneEngine.createOrUpdateIndexesForFile(indexWriter, f, folder);
            indexWriter.commit();
            return true;
        } catch (IOException | SAXException ex) {
            Logger.getLogger(LuceneIndexingTask.class.getSimpleName()).info(ex.getMessage());
            return false;
        }
    }
}
