package moon.bitter.Mapper;

import moon.bitter.Pojo.Data;
import moon.bitter.Utils.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface BookMapper {


    @Select("select city name, city_cnt value from book_city_cnt")
    ArrayList<Data> getChinaMapData();


    @Select("select press name , press_count value from book_press_cnt")
    ArrayList<Data> getPressData();

    @Select("select region name , value from book_region_cnt")
    ArrayList<Data> getRegionData();

    @Select("select type name , type_cnt value from book_type_cnt")
    ArrayList<Data> getTypeData();

    @Select("select year name , year_cnt value from book_year_cnt")
    ArrayList<Data> getYearData();

    @Select("select grads name , `count(grads)` value from book_page_cnt")
    ArrayList<Data> getPageData();

    @Select("select price_grade name , price_count value from book_price_cnt")
    ArrayList<Data> getPriceData();
}
