package moon.bitter.Service;

import moon.bitter.Pojo.Data;
import moon.bitter.Utils.Result;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.ArrayList;

public interface BookService {
    Result getChinaMapData();

    Result getBookPress();

    Result getBookRegion();

    Result getBookType();

    Result getBookYear();

    Result getBookPage();

    Result getBookPrice();
}
