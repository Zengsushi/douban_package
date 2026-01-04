package moon.bitter.Controller;

import moon.bitter.Pojo.*;

import moon.bitter.Service.MovieService;
import moon.bitter.Utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    /**
     * 基本功能
     * @return
     */
    // 管理员获取电影列表
    @GetMapping("/movieInfo")
    public Result<ArrayList<Map<String, Movie>>> getMovieInfo() {
        return movieService.getInfo();
    }

    // 用户获取电影列表
    @PostMapping("movieList")
    public Result<ArrayList<Map<String, Movie>>> getMovieList(@RequestBody Page page) {
        return movieService.getMovieList(page);
    }

    // 管理员添加电影
    @PostMapping("/addMovie")
    public Result addMovie(@RequestBody Movie movie) {
        return movieService.addMovie(movie);
    }

    // 管理员删除电影
    @PostMapping("deleteMovie")
    public Result deleteMovie(@RequestBody Movie movie) {
        return movieService.deleteMovie(movie);
    }

    // 管理员更新电影
    @PostMapping("updateMovie")
    public Result updateMovie(@RequestBody Movie movie) {
        return movieService.updateMovie(movie);
    }

    // 搜索电影
    @PostMapping("searchMovie")
    public Result searchMovie(@RequestBody Page page) {
        return movieService.searchMovie(page);
    }

    // 获取电影地区和国家
    @GetMapping("regionList")
    public Result<ArrayList<Map<String, Region>>> getRegion() {
        return movieService.getRegion();
    }

    // 获取电影类型
    @GetMapping("typeList")
    public Result<ArrayList<Map<String, Type>>> getType() {
        return movieService.getType();
    }

    // 数据中存在的电影数
    @GetMapping("movieCount")
    public Result movieCount() {
        return movieService.getMovieCount();
    }

    // 电影详细信息
    @PostMapping("info")
    public Result getInfo(@RequestBody Movie movie) {
        return movieService.getInfos(movie.getId());
    }


    /***
     * 业务功能
     *
     *
     */
    // 电影点击埋点
    @PostMapping("clickMovie")
    public Result clickMovie(@RequestBody Movie movie) {
        return movieService.clickMovie(movie);
    }

    // 点击前15 电影信息
    @GetMapping("clickTop10")
    public Result clickTop10() {
        return movieService.clickTop10();
    }

    // 时长饼图
    @GetMapping("getCategory")
    public Result GetCategory() {
        return movieService.getCategory();
    }

    // 评分分布柱状图
    @GetMapping("getScore")
    public Result GetScoreCnt() {
        return movieService.getScoreCnt();
    }

    // 类型词云图
    @GetMapping("getTypeCnt")
    public Result GetTypeCnt() {
        return movieService.getTypeCnt();
    }

    @GetMapping("getRegionCnt")
    public Result GetRegionCnt() {
        return movieService.getRegionCnt();
    }

    @GetMapping("getScoreCnt")
    public Result getScoreCnt() {
        return movieService.getscoreCnt();
    }

    @GetMapping("getNumberCnt")
    public Result getRegionNumberCnt() {
        return movieService.getRegionNumberCnt();
    }

    @GetMapping("getYearCnt")
    public Result getYearCnt() {
        return movieService.getYearCnt();
    }

    @PostMapping("getFinBbyTitle")
    public Result getFindByTitle(@RequestBody Movie movie) {
        return movieService.getFindByTitle(movie);
    }

    @GetMapping("getTop250")
    public Result getTop250() {
        return movieService.getTop250();
    }
}
