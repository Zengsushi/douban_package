package moon.bitter.Pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class Movie {

    private String id;
    private String title;
    private String year;
    private String duration;
    private String director;
    private String author;
    private String actor;
    private String region;
    private String type;
    private String score;
    private String url;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}
