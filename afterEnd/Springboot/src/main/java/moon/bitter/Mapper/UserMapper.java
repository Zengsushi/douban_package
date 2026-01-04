package moon.bitter.Mapper;

import moon.bitter.Pojo.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.plugin.Interceptor;

@Mapper
public interface UserMapper {

    @Select("select * from admin where name = #{name}")
    public User GetByName(String name);

    @Insert("insert into user(name , pass) values(#{name} , #{pass})")
    void register(@Param("name") String name, @Param("pass") String pass);

    @Select("select * from user where name = #{name}")
    User getByUserName(String name);

    @Select("select * from user where id = #{id}  ;")
    User getInfo(String id);

    @Select("select nickname from user where id = #{userId}")
    String getUserInfo(String userId);

    @Insert("insert into user_login(user_id , login_time , out_time) values(#{id} , now() , now() )")
    void addLogin(String id);

    @Select("select id from user_login where user_id = #{id} order by login_time desc limit 1")
    Integer getUserLoginId(String id);

    @Update("update user_login set out_time = now() where id = #{id} ;")
    void updateLogin(Integer id);


    @Select("select id , name , nickname , email , gender , birth , create_time from user where id = #{id}")
    User UserById(String id);

    @Update("update user set nickname = #{nickName} , email = #{email} , gender = #{gender} , birth = #{birth} where id = #{id} ;")
    void updateUserInfo(User user);
}
