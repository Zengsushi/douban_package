package moon.bitter.Pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MvRemark {
    private Integer id ;
    private String userId;
    private String mvId  ;
    private String content  ;
    private String userStar ;
    private String praise ;
    private String stamp ;
    private LocalDateTime createTime ;
}
