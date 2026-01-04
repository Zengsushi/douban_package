package moon.bitter.Controller;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import moon.bitter.Config.SpringbootKafkaConfig;
import moon.bitter.Pojo.User;
import moon.bitter.Service.UserService;
import moon.bitter.Utils.Result;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    // 管理员登录
    @PostMapping("/adminLogin")
    public Result login(@RequestBody User user) {
        return userService.login(user);
    }

    // 用户注册
    @PostMapping("/userRegister")
    public Result register(@RequestBody User user) {
        return userService.register(user);
    }

    // 用户登录 (登录前设置登录时间和登出时间,防止异常登出导致用户登出时间为空)
    @PostMapping("/userLogin")
    public Result userLogin(@RequestBody User user) {
        return userService.userLogin(user);
    }

    // 用户退出登录(更新最后一次登录的登出时间)
    @PostMapping("userLogOut")
    public Result userLogOut(@RequestBody User user) {
        return userService.logOut(user);
    }

    // 用户信息
    @PostMapping("/userInfo")
    public Result userInfo(@RequestBody User u) {
        return userService.userInfo(u.getId());
    }

    // 获取用户信息
    @PostMapping("/getUserInfo")
    public Result getUserInfo(@RequestBody User user) {
        return userService.getUserInfo(user.getId());
    }

    // 用户信息更新
    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }

//    @PostMapping("adminLogOut")
//    public Result adminLogOut(@RequestBody User user) {
//        return userService.logOut(user);
//    }


}
