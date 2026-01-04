package moon.bitter.Service.impl;

import moon.bitter.Mapper.BookMapper;
import moon.bitter.Pojo.Data;
import moon.bitter.Service.BookService;
import moon.bitter.Utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Result getChinaMapData() {
        try {
            ArrayList<Data> list = bookMapper.getChinaMapData();
            return Result.success(list);
        }catch (Exception e){
            return Result.error("服务器响应异常!") ;
        }
    }

    @Override
    public Result getBookPress() {
        try {
            ArrayList<Data> list = bookMapper.getPressData();
            return Result.success(list);
        }catch (Exception e){
            return Result.error("服务器响应异常!") ;
        }
    }

    @Override
    public Result getBookRegion() {
        try {
            ArrayList<Data> list = bookMapper.getRegionData();
            return Result.success(list);
        }catch (Exception e){
            return Result.error("服务器响应异常!") ;
        }
    }

    @Override
    public Result getBookType() {
        try {
            ArrayList<Data> list = bookMapper.getTypeData();
            return Result.success(list);
        }catch (Exception e){
            return Result.error("服务器响应异常!") ;
        }
    }

    @Override
    public Result getBookYear() {
        try {
            ArrayList<Data> list = bookMapper.getYearData();
            return Result.success(list);
        }catch (Exception e){
            return Result.error("服务器响应异常!") ;
        }
    }

    @Override
    public Result getBookPage() {
//        try {
            ArrayList<Data> list = bookMapper.getPageData();
            return Result.success(list);
//        }catch (Exception e){
//            return Result.error("服务器响应异常!") ;
//        }
    }

    @Override
    public Result getBookPrice() {
//        try {
            ArrayList<Data> list = bookMapper.getPriceData();
            return Result.success(list);
//        }catch (Exception e){
//            return Result.error("服务器响应异常!") ;
//        }
    }
}
