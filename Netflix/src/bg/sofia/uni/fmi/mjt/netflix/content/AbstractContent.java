package bg.sofia.uni.fmi.mjt.netflix.content;

import bg.sofia.uni.fmi.mjt.netflix.content.enums.Genre;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;

public abstract class AbstractContent implements Streamable {

    public AbstractContent(String name, Genre genre, PgRating rating) {
        this.name = name;
        this.genre = genre;
        this.rating = rating;
        this.views = 0;
    }

    /**
     * @return the title of the streamable content.
     */
    @Override
    public String getTitle() {
        return name;
    }

    /**
     * @return the genre of the streamable content.
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * @return the content duration in minutes.
     */
    public abstract int getDuration();

    /**
     * @return the PG rating of the streamable content.
     */
    @Override
    public PgRating getRating() {
        return rating;
    }

    public int getViews() {
        return views;
    }

    public void increaseViewCount()
    {
        views++;
    }

    private String name;
    private Genre genre;
    private PgRating rating;
    private int views;
}
