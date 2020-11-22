package bg.sofia.uni.fmi.mjt.netflix.content;

public class Episode {
    Episode(String name, int duration)
    {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    private String name;
    private int duration;
}
