package API.StopTime;

/**
 * Created by Andréas on 07/04/2018.
 */



        import java.util.List;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class StopTime {

    @SerializedName("pattern")
    @Expose
    private Pattern pattern;
    @SerializedName("times")
    @Expose
    private List<Time> times = null;

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public List<Time> getTimes() {
        return times;
    }

    public void setTimes(List<Time> times) {
        this.times = times;
    }

}




