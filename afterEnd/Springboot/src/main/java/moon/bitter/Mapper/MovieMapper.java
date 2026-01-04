package moon.bitter.Mapper;


import moon.bitter.Pojo.*;
import moon.bitter.Utils.Result;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.plugin.Interceptor;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface MovieMapper {
    @Select("select * from Movies limit 10")
    ArrayList<Map<String, Movie>> getInfo();

    @Insert("insert into Movies(id , title)" +
            " values(#{id} , #{title})")
    void addMovie(@Param("id") String id, @Param("title") String title);

    @Delete("delete from Movies where id = #{id}")
    void deleteMovie(@Param("id") String id);

    @Update("update Movies set title = #{m.title}  ," +
            "year = #{m.year} , " +
            "region= #{m.region} , " +
            "type = #{m.type} ," +
            "director = #{m.director} ," +
            "ctors = #{m.ctors} , " +
            "count = #{m.count} ," +
            "score = #{m.score} ," +
            "star = #{m.star} , " +
            "url = #{m.url}" +
            "where id = #{m.id}")
    void updateMovie(@Param("m") Movie movie);

    @Select("<script>"
            + "SELECT * FROM Movies "
            + "WHERE 1=1 "
            + "<if test='title != null and title != \"\"'>"
            + "AND title LIKE CONCAT('%', #{title}, '%') "
            + "</if>"
            + "<if test='region != null and region != \"\"'>"
            + "AND region LIKE CONCAT('%', #{region}, '%') "
            + "</if>"
            + "<if test='type != null and type != \"\"'>"
            + "AND type LIKE CONCAT('%', #{type}, '%') "
            + "</if>"
            + "</script>")
    ArrayList<Map<String, Movie>> keywordByMovies(@Param("title") String title,
                                                  @Param("region") String region,
                                                  @Param("type") String type);

    @Select("select * from region")
    ArrayList<Map<String, Region>> getRegion();

    @Select("select * from type")
    ArrayList<Map<String, Type>> getType();

    @Select("select count(*) from Movies")
    Integer getMovieCount();

    @Select("select * from Movies limit #{start} offset #{end}")
    ArrayList<Map<String, Movie>> getMovieList(int start, int end);

    @Select("select title , score from mv_click_top10 order by count desc  limit 15;")
    ArrayList<Map<String, Map<String, String>>> clickTop10();

    @Select("select * from Movies where id = #{id}")
    MvInfo getMvInfo(String id);


    @Select("select category name, category_cnt value from category")
    ArrayList<Map<String, Category>> getCategory();

    @Select("select score name , score_cnt value from score_cnt")
    ArrayList<Map<String, Data>> getScoreCnt();

    @Select("select type name , type_cnt value from type_cnt")
    ArrayList<Map<String, Data>> getTypeCnt();

    @Select("select region " +
            "from region_cnt " +
            "where about = #{about}" +
            "group by region limit 20")
    ArrayList<String> getRegionList(String about);

    @Select("select score from region_cnt where region = #{s} and score > 4.0  " +
            "group by score limit 2;")
    ArrayList<String>getStar(String s);

    @Select("select REGEXP_REPLACE(title, '[^\\\\u4e00-\\\\u9fa5]', '') as name " +
            "from region_cnt " +
            "where region = #{s}  " +
            "and score = #{s1} " +
            "limit 2")
    ArrayList<Map<String, String>> getMovie(String s, String s1);

    @Select("select score name , count(*) value from region_cnt group by score ")
    ArrayList<Map<String, Data>> getscoreCnt();

    @Select(" select about name, count(about) value " +
            " from region_cnt " +
            " group by about")
    ArrayList<Map<String, Data>> getRegionNumberCnt();

    @Select("select * from year_cnt order by year")
    ArrayList<Map<String, Data>> getYearCnt();

    @Select("select id from Movies where title like #{title}")
    Movie getFindByTitle(String title);

    @Select("select  name , value from mv_score_count_top250 limit 13")
    ArrayList<Map<String, String>> getTop250();
}
