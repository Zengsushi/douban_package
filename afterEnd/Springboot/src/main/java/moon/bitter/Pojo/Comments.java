package moon.bitter.Pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comments {

    // 评论
    private String id;
    private String mvId;
    private String content;
    private String userId;
    private Integer reCount;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

}
