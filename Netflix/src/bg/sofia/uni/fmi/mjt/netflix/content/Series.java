package bg.sofia.uni.fmi.mjt.netflix.content;

import bg.sofia.uni.fmi.mjt.netflix.content.enums.*;

public class Series extends AbstractContent{

    public Series(String name, Genre genre, PgRating rating, Episode[] episodes) {
        super(name, genre, rating);
        this.episodes = episodes;
    }

    /**
     * @return the content duration in minutes.
     */
    @Override
    public int getDuration() {

        int durationOfSeries = 0;
        for(Episode episode : episodes)
        {
            durationOfSeries+=episode.getDuration();
        }
        return durationOfSeries;
    }

    private Episode[] episodes;

}
