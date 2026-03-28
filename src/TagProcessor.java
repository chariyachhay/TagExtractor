import java.io.*;
import java.util.*;

public class TagProcessor {

    private Set<String> stopWords;
    private Map<String, Integer> map;

    public TagProcessor() {
        stopWords = new TreeSet<>();
        map = new TreeMap<>();
    }

    public void loadStopWords(File file) throws IOException {
        stopWords.clear();

        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            String word = sc.nextLine().trim().toLowerCase();

            if (!word.isEmpty()) {
                stopWords.add(word);
            }
        }

        sc.close();
    }

    public void processFile(File file) throws IOException {
        map.clear();

        Scanner sc = new Scanner(file);

        while (sc.hasNext()) {
            String word = sc.next();

            word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();

            if (word.length() == 0) continue;

            if (stopWords.contains(word)) continue;

            if (map.containsKey(word)) {
                map.put(word, map.get(word) + 1);
            } else {
                map.put(word, 1);
            }
        }

        sc.close();
    }

    public Map<String, Integer> getMap() {
        return map;
    }
}