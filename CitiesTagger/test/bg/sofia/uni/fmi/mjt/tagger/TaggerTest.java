package bg.sofia.uni.fmi.mjt.tagger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TaggerTest {

    private Reader csvReader;
    private static final Path csvFilePath = Path.of("world-cities.csv");

    @Before
    public void loadInputReader() {
        try {
            csvReader = Files.newBufferedReader(csvFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void closeInputReader() {
        try {
            if (csvReader != null) {
                csvReader.close();
            }

        } catch (IOException e) {
            throw new RuntimeException();

        }
    }

    @Test
    public void testTagCitiesNoCities() throws IOException {
        String inputString = "there are not cities here \n neither are there here ";

        Reader inputReader = new StringReader(inputString);
        Writer outputWriter = new StringWriter();

        Tagger newTagger = new Tagger(csvReader);

        newTagger.tagCities(inputReader, outputWriter);
        String outputString = outputWriter.toString();
        assertEquals(inputString, outputString);

    }

    @Test
    public void testTagCitiesOneCity() throws IOException {
        String inputString = "Only on city Sofia \n neither are there here ";
        String expectedString = "Only on city <city country=\"Bulgaria\">Sofia</city> \n neither are there here ";
        Reader inputReader = new StringReader(inputString);
        Writer outputWriter = new StringWriter();

        Tagger newTagger = new Tagger(csvReader);

        newTagger.tagCities(inputReader, outputWriter);
        String outputString = outputWriter.toString();
        assertEquals(expectedString, outputString);

    }

    @Test
    public void testTagCitiesOneCityPunctuated() throws IOException {
        String inputString = "Only on city ?Sofia's center. \n Neither are there here. ";
        String expectedString = "Only on city ?<city country=\"Bulgaria\">Sofia</city>'s center. \n Neither are there here. ";
        Reader inputReader = new StringReader(inputString);
        Writer outputWriter = new StringWriter();

        Tagger newTagger = new Tagger(csvReader);

        newTagger.tagCities(inputReader, outputWriter);
        String outputString = outputWriter.toString();
        assertEquals(expectedString, outputString);

    }

    @Test
    public void testGetAllTagsCountDefaultFile() throws IOException {
        String inputString = "Sofia is smaller than Berlin, but Berlin is smaller than Moscow.";

        Reader inputReader = new StringReader(inputString);
        Writer outputWriter = new StringWriter();

        Tagger newTagger = new Tagger(csvReader);
        newTagger.tagCities(inputReader, outputWriter);
        assertEquals(4, newTagger.getAllTagsCount());
    }

    @Test
    public void testGetAllTagsCountNoCities() throws IOException {
        String inputString = "there are not cities here";

        Reader inputReader = new StringReader(inputString);
        Writer outputWriter = new StringWriter();

        Tagger newTagger = new Tagger(csvReader);
        newTagger.tagCities(inputReader, outputWriter);
        assertEquals(0, newTagger.getAllTagsCount());
    }

    @Test
    public void testGetAllTaggedCitiesDefaultFile() throws IOException {
        String inputString = "Sofia is smaller than Berlin, but Berlin is smaller than Moscow.";

        Reader inputReader = new StringReader(inputString);
        Writer outputWriter = new StringWriter();
        Tagger newTagger = new Tagger(csvReader);

        newTagger.tagCities(inputReader, outputWriter);

        Collection<String> tagged = new LinkedList<String>(Arrays.asList("Berlin", "Sofia", "Moscow"));
        assertArrayEquals(tagged.toArray(), newTagger.getAllTaggedCities().toArray());
    }

    @Test
    public void testGetAllTaggedCitiesNoCities() throws IOException {
        String inputString = "there are no cities here";

        Reader inputReader = new StringReader(inputString);
        Writer outputWriter = new StringWriter();
        Tagger newTagger = new Tagger(csvReader);

        newTagger.tagCities(inputReader, outputWriter);

        Collection<String> tagged = new LinkedList<String>();
        assertArrayEquals(tagged.toArray(), newTagger.getAllTaggedCities().toArray());
    }

    @Test
    public void testGetNMostTaggedCitiesDefaultFile() throws IOException {
        String inputString = "Sofia, Sofia, Berlin is smaller than Berlin, but Berlin is smaller than Moscow.";

        Reader inputReader = new StringReader(inputString);
        Writer outputWriter = new StringWriter();
        Tagger newTagger = new Tagger(csvReader);

        newTagger.tagCities(inputReader, outputWriter);
        Collection<String> mostTagged = new LinkedList<String>(Arrays.asList("Berlin", "Sofia"));
        assertArrayEquals(mostTagged.toArray(), newTagger.getNMostTaggedCities(2).toArray());
    }

    @Test
    public void testGetNMostTaggedCitiesNoCities() throws IOException {
        String inputString = "there are no cities here";

        Reader inputReader = new StringReader(inputString);
        Writer outputWriter = new StringWriter();
        Tagger newTagger = new Tagger(csvReader);

        newTagger.tagCities(inputReader, outputWriter);
        Collection<String> mostTagged = new LinkedList<String>();
        assertArrayEquals(mostTagged.toArray(), newTagger.getNMostTaggedCities(2).toArray());
    }


}
