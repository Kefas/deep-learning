import java.io.IOException;

/**
 * Created by sg0222871 on 1/15/17.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        DeepLearing4J deepLearing4J = new DeepLearing4J("data/text.txt");
        deepLearing4J = new DeepLearing4J("data/lematyzacja.txt");
        deepLearing4J.process();
    }
}
