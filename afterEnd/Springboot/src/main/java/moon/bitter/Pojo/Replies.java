package moon.bitter.Pojo;

import lombok.Data;

@Data
public class Replies {
    private String id;
    private String content;
    private String userId;
    private String nickname  ; // 额外字段 ->  用户昵称
    private Integer commentId;
    private String createTime;
}
