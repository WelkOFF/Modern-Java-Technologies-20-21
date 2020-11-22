package bg.sofia.uni.fmi.mjt.netflix.content;

import bg.sofia.uni.fmi.mjt.netflix.content.enums.*;

public class Movie extends AbstractContent {

    public Movie(String name, Genre genre, PgRating rating, int duration)
    {
        super(name,genre,rating);
        this.duration = duration;
    }

    /**
     * @return the content duration in minutes.
     */
    @Override
    public int getDuration() {
        return duration;
    }

    private String name;
    private Genre genre;
    private PgRating rating;
    private int duration;
}
