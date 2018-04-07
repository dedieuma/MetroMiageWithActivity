package API;

import java.util.List;

import API.StopTime.StopTime;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Andréas on 05/04/2018.
 */

public interface MetroInterface {

    @GET("api/routers/default/index/routes")
    Call<List<Route>> getRoutes();

    @GET("api/routers/default/index/routes/{name}/clusters")
    Call<List<Arret>> getArrets(@Path("name") String name);

    @GET("api/routers/default/index/clusters/{name}/stoptimes")
    Call<List<StopTime>> getStopTimes(@Path("name") String name);
}
