package moon.bitter.Pojo;

import lombok.Data;

@Data
public class Page {
    private String title;
    private String region;
    private String type;
    private int total ;
    private int size ;
    private int page ;
}
