package moon.bitter.Service;

import moon.bitter.Pojo.User;
import moon.bitter.Utils.Result;

public interface UserService {
    Result login(User user);

    Result register(User user);

    Result userLogin(User user);

    Result userInfo(String id);

    Result logOut(User user);


    Result getUserInfo(String id);

    Result updateUserInfo(User user);
}
