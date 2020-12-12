package bg.sofia.uni.fmi.mjt.tagger;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Collection;
import java.util.Arrays;


public class Tagger {

    private final Map<String, String> citiesCountryRegister;
    private Map<String, Integer> tagCount;
    private long totalTagCount;

    /**
     * Creates a new instance of Tagger for a given list of city/country pairs
     *
     * @param citiesReader a java.io.Reader input stream containing list of cities and countries
     *                     in the specified CSV format
     */
    public Tagger(Reader citiesReader) {
        this.citiesCountryRegister = createCitiesCountryRegister(citiesReader);
        this.tagCount = new HashMap<String, Integer>();
        this.totalTagCount = 0;

    }

    /**
     * Processes an input stream of a text file, tags any cities and outputs result
     * to a text output stream.
     *
     * @param text   a java.io.Reader input stream containing text to be processed
     * @param output a java.io.Writer output stream containing the result of tagging
     */
    public void tagCities(Reader text, Writer output) throws IOException {

        this.tagCount = new HashMap<String, Integer>();

        this.totalTagCount = 0;

        try (BufferedReader br = new BufferedReader(text)) {
            String line;
            boolean notFirstTime = false;
            while ((line = br.readLine()) != null) {

                String processedLine = processLine(line);
                if (notFirstTime) {
                    processedLine = "\n" + processedLine;
                }
                output.write(processedLine);
                notFirstTime = true;
            }
            output.flush();

        }
    }

    /**
     * Returns a collection the top @n most tagged cities' unique names
     * from the last tagCities() invocation. Note that if a particular city has been tagged
     * more than once in the text, just one occurrence of its name should appear in the result.
     * If @n exceeds the total number of cities tagged, return as many as available
     * If tagCities() has not been invoked at all, return an empty collection.
     *
     * @param n the maximum number of top tagged cities to return
     * @return a collection the top @n most tagged cities' unique names
     * from the last tagCities() invocation.
     */
    public Collection<String> getNMostTaggedCities(int n) {


        List<String> mostTaggedCities = new LinkedList<String>(tagCount.keySet());
        n = Math.min(n, mostTaggedCities.size());
        mostTaggedCities.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return tagCount.get(o2).compareTo(tagCount.get(o1));
            }
        });

        return mostTaggedCities.subList(0, n);
    }

    /**
     * Returns a collection of all tagged cities' unique names
     * from the last tagCities() invocation. Note that if a particular city has been tagged
     * more than once in the text, just one occurrence of its name should appear in the result.
     * If tagCities() has not been invoked at all, return an empty collection.
     *
     * @return a collection of all tagged cities' unique names
     * from the last tagCities() invocation.
     */
    public Collection<String> getAllTaggedCities() {
        return tagCount.keySet();
    }

    /**
     * Returns the total number of tagged cities in the input text
     * from the last tagCities() invocation
     * In case a particular city has been taged in several occurences, all must be counted.
     * If tagCities() has not been invoked at all, return 0.
     *
     * @return the total number of tagged cities in the input text
     */
    public long getAllTagsCount() {
        return totalTagCount;
    }

    private Map<String, String> createCitiesCountryRegister(Reader citiesReader) {

        Map<String, String> citiesCountryRegister = new HashMap<String, String>();
        try (BufferedReader br = new BufferedReader(citiesReader)) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] entry = line.split(",");
                citiesCountryRegister.put(entry[0], entry[1]);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return citiesCountryRegister;
    }

    private String processLine(String line) {
        if (line == null) {
            throw new IllegalArgumentException();
        }
        List<String> wordCollection = new LinkedList<String>(Arrays.asList(line.split(" |-|:|;|,|'|\\?|!|\\.")));

        Set<String> citySet = new LinkedHashSet<String>();

        for (String originalWord : wordCollection) {
            if (originalWord.length() > 0) {
                String modifiedWord = convertToProperCase(originalWord);
                if (citiesCountryRegister.containsKey(modifiedWord)) {
                    totalTagCount++;
                    if (tagCount.containsKey(modifiedWord)) {
                        Integer count = tagCount.get(modifiedWord);
                        tagCount.replace(modifiedWord, count, count + 1);
                    } else {
                        tagCount.put(modifiedWord, 1);
                    }
                    line = line.replaceAll(originalWord, modifiedWord);
                    citySet.add(modifiedWord);
                }
            }

        }

        for (String cityName : citySet) {
            String countryName = citiesCountryRegister.get(cityName);

            String tag = "<city country=\"" + countryName + "\">" + cityName + "</city>";

            line = line.replaceAll(cityName, tag);

        }

        return line;
    }

    private static String convertToProperCase(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }

        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

}