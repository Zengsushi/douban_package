package moon.bitter.Service;

import moon.bitter.Pojo.Replies;
import moon.bitter.Pojo.*;
import moon.bitter.Utils.Result;

import java.util.ArrayList;
import java.util.Map;

public interface MovieService {

    Result<ArrayList<Map<String, Movie>>> getInfo();

    Result addMovie(Movie movie);

    Result deleteMovie(Movie movie);

    Result updateMovie(Movie movie);

    Result searchMovie(Page page);

    Result<ArrayList<Map<String, Region>>> getRegion();

    Result<ArrayList<Map<String, Type>>> getType();

    Result getMovieCount();

    Result<ArrayList<Map<String, Movie>>> getMovieList(Page page);

    Result clickMovie(Movie movie);

    Result clickTop10();

    Result getInfos(String id);


    Result getCategory();

    Result getScoreCnt();

    Result getTypeCnt();

    Result getRegionCnt();

    Result getscoreCnt();

    Result getRegionNumberCnt();

    Result getYearCnt();

    Result getFindByTitle(Movie movie);

    Result getTop250();
}
