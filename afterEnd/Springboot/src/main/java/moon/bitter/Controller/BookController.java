package moon.bitter.Controller;

import moon.bitter.Pojo.Data;
import moon.bitter.Service.BookService;
import moon.bitter.Utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService  bookService;

    @GetMapping("/chinaMap")
    public Result getChinaMapData(){
        return bookService.getChinaMapData();
    }


    @GetMapping("/bookPress")
    public Result getBookPress(){
        return bookService.getBookPress();
    }

    @GetMapping("/bookRegion")
    public Result getBookRegion(){
        return bookService.getBookRegion();
    }

    @GetMapping("/bookType")
    public Result getBookType(){
        return bookService.getBookType();
    }

    @GetMapping("/bookYear")
    public Result getBookYear(){
        return bookService.getBookYear();
    }

    @GetMapping("/constbookPage")
    public Result getBookPageSize(){
        return bookService.getBookPage();
    }

    @GetMapping("/bookPrice")
    public Result getBookPrice(){
        return bookService.getBookPrice();
    }


}
