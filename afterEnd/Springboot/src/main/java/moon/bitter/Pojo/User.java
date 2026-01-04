package moon.bitter.Pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    private String id;
    private String name;
    private String pass;
    private String nickName;
    private String email;
    private String gender;
    private String birth;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime loginTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime outTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;
}
