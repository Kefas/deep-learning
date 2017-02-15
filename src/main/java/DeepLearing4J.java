import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by sg0222871 on 2/13/17.
 */
public class DeepLearing4J {

    private final File file;

    public DeepLearing4J(String path) {
        this.file = new File(path);
    }

    public void process() throws IOException {

        //LOADING DATA
        System.out.println("Load & Vectorize Sentences....");
        SentenceIterator iter = new LineSentenceIterator(file);

        //TOKENIZING THE DATA
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        //TRAINING THE MODEL
        System.out.println("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        System.out.println("Fitting Word2Vec model....");
        vec.fit();

        Collection<String> lst = vec.wordsNearest("dom", 10);
        System.out.println("10 Words closest to 'dom'\n: " + lst);

        lst = vec.wordsNearest("komputer", 10);
        System.out.println("10 Words closest to 'komputer'\n: " + lst);

        lst = vec.wordsNearest("piątek", 10);
        System.out.println("10 Words closest to 'piątek'\n: " + lst);

        lst = vec.wordsNearest("żona", 10);
        System.out.println("10 Words closest to 'żona'\n: " + lst);

        lst = vec.wordsNearest("policjant", 10);
        System.out.println("10 Words closest to 'policjant'\n: " + lst);
    }
}
