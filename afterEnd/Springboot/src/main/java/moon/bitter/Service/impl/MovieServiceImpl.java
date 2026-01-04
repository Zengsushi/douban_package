package moon.bitter.Service.impl;

import cn.hutool.json.JSONUtil;
import moon.bitter.Config.SpringbootKafkaConfig;
import moon.bitter.Mapper.UserMapper;
import moon.bitter.Pojo.Replies;
import moon.bitter.Mapper.MovieMapper;
import moon.bitter.Pojo.*;
import moon.bitter.Service.MovieService;
import moon.bitter.Utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private static final Logger log = LoggerFactory.getLogger(MovieServiceImpl.class);
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Result<ArrayList<Map<String, Movie>>> getInfo() {
        ArrayList<Map<String, Movie>> list = movieMapper.getInfo();
        return Result.success(list);
    }

    @Override
    public Result addMovie(Movie movie) {
        try {
            // 新增电影 日志
            String obj2String = JSONUtil.toJsonStr(movie);
            kafkaTemplate.send(SpringbootKafkaConfig.TOPIC_ADD_MOVIE_LOG, obj2String);
            movieMapper.addMovie(movie.getId(), movie.getTitle());
        } finally {
            return Result.success();
        }
    }

    @Override
    public Result deleteMovie(Movie movie) {
        try {
            movieMapper.deleteMovie(movie.getId());
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除失败");
        }
    }

    @Override
    public Result updateMovie(Movie movie) {
        try {
            movieMapper.updateMovie(movie);
            return Result.success();
        } catch (Exception e) {
            return Result.error("修改失败");
        }
    }

    @Override
    public Result searchMovie(Page page) {
        try {
            ArrayList<Map<String, Movie>> list = movieMapper.keywordByMovies(page.getTitle(), page.getRegion(),
                    page.getType());
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("响应异常!");
        }
    }

    @Override
    public Result<ArrayList<Map<String, Region>>> getRegion() {
        try {
            ArrayList<Map<String, Region>> list = movieMapper.getRegion();
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("响应异常!");
        }
    }

    @Override
    public Result<ArrayList<Map<String, Type>>> getType() {
        try {
            ArrayList<Map<String, Type>> list = movieMapper.getType();
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("响应异常!");
        }
    }

    @Override
    public Result getMovieCount() {
        Integer cnt = movieMapper.getMovieCount();
        return Result.success(cnt);
    }

    // 分页查询
    @Override
    public Result<ArrayList<Map<String, Movie>>> getMovieList(Page page) {
        int start = (page.getPage() - 1) * page.getSize();
        ArrayList<Map<String, Movie>> list = movieMapper.getMovieList(page.getSize(), start);
        return Result.success(list);
    }

    @Override
    public Result clickMovie(Movie movie) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("id", movie.getId());
            map.put("title", movie.getTitle());
            map.put("score", movie.getScore());
            String str = JSONUtil.toJsonStr(map);
            kafkaTemplate.send(SpringbootKafkaConfig.TOPIC_CLICK_MOVIE_LOG, str);
            return Result.success();
        } catch (Exception e) {
            return Result.error("kafka访问异常!");
        }

    }

    @Override
    public Result clickTop10() {
        ArrayList<Map<String, Map<String, String>>> list = movieMapper.clickTop10();
        return Result.success(list);
    }


    @Override
    public Result getInfos(String id) {
        if (id == null) {
            return Result.error("参数错误");
        }
        MvInfo mv = movieMapper.getMvInfo(id);
        return Result.success(mv);
    }


    @Override
    public Result getCategory() {
        ArrayList<Map<String, Category>> arr = movieMapper.getCategory();
        return Result.success(arr);
    }

    @Override
    public Result getScoreCnt() {
        ArrayList<Map<String, Data>> arr = movieMapper.getScoreCnt();
        return Result.success(arr);
    }

    @Override
    public Result getTypeCnt() {
        ArrayList<Map<String, Data>> arr = movieMapper.getTypeCnt();
        return Result.success(arr);
    }

    @Override
    public Result getRegionCnt() {
        ArrayList<Map<String, Object>> regionArr = new ArrayList<>();

        Map<String, Object> withinMap = new HashMap<>();
        Map<String, Object> outerMap = new HashMap<>();
        Map<String, Object> styleMap = new HashMap<>();
        // 一级分区 国内 , 国外 -> 二级分区 国家 -> 三级分区 评分 -> 电影名称
        withinMap.put("name", "国内");
        outerMap.put("name", "国外");

        regionArr.add(withinMap);
        regionArr.add(outerMap);

        styleMap.put("color", "#1E90FF");
        String[] arr = {"#0FF" , "#1E90FF"};
        int index = 0;
        for (Map<String, Object> map : regionArr) {
            ArrayList<String> list = movieMapper.getRegionList(map.get("name").toString());
            ArrayList<Map<String, Object>> arr2 = new ArrayList<>();
            map.put("itemStyle", arr[index]);
            for (String s : list) {
                Map<String, Object> m1 = new HashMap<>();
                m1.put("name", s);
                arr2.add(m1);
                map.put("children", arr2);
                ArrayList<String> list1 = movieMapper.getStar(s);
                ArrayList<Map<String, Object>> arr3 = new ArrayList<>();
                for (String s1 : list1) {
                    Map<String, Object> m2 = new HashMap<>();
                    m2.put("name", s1);
                    arr3.add(m2);
                    m1.put("children", arr3);
                    ArrayList<Map<String, String>> list2 = movieMapper.getMovie(s, s1);
                    m2.put("children", list2);
                }
            }
            index++;
        }
        return Result.success(regionArr);


    }

    @Override
    public Result getscoreCnt() {
        ArrayList<Map<String, Data>> map  = movieMapper.getscoreCnt() ;
        return Result.success(map);
    }

    @Override
    public Result getRegionNumberCnt() {
        ArrayList<Map<String, Data>> map  = movieMapper.getRegionNumberCnt() ;
        return Result.success(map);
    }

    @Override
    public Result getYearCnt() {
        ArrayList<Map<String , Data>> map = movieMapper.getYearCnt();
        return Result.success(map);
    }

    @Override
    public Result getFindByTitle(Movie movie) {
        Movie m = movieMapper.getFindByTitle(movie.getTitle()+ "%") ;
        return Result.success(m.getId());
    }

    @Override
    public Result getTop250() {
        ArrayList<Map<String,String>> list = movieMapper.getTop250() ;
        return Result.success(list);
    }

}


