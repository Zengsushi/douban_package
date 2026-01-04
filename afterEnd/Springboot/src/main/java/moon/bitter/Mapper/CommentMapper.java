package moon.bitter.Mapper;

import moon.bitter.Pojo.Comments;
import moon.bitter.Pojo.Replies;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface CommentMapper {

    // 评论添加
    @Insert("insert into comments(mv_id , content , user_id , re_count , create_time) values( " +
            " #{c.mvId} , #{c.content} , #{c.userId} , 0 , now() )")
    void addComments(@Param("c") Comments comments);

    // 获取评论列表
    @Select("select * from comments where mv_id = #{c.mvId} ")
    ArrayList<Comments> getCommentsList(@Param("c") Comments comments);

    // 添加回复
    @Insert("insert into replies( content , user_id , comment_id , create_time) " +
            " values( #{r.content} , #{r.userId} , #{r.commentId} , now() )")
    void addRepliesCount(@Param("r") Replies replies);

    // 获取制定的单评论
    @Select("select * from comments where id = #{commentId}")
    Comments getComments(Integer commentId);

    // 更新评论回复数
    @Update("update comments set re_count = #{c.reCount} where id = #{c.id}")
    void updateCommentsCount(@Param("c") Comments comments);

    // 删除评论回复
    @Delete("delete from replies where id = #{id}")
    void deleteReplies(String id);

    // 删除评论
    @Delete("delete from comments where id = #{id}")
    void deleteComments(String id);

    // 对删除的评论 删除评论的回复
    @Delete("delete from replies where comment_id = #{id}")
    void initReplies(String id);

    // 获取评论的回复
    @Select("select * from replies where comment_id = #{commentId}")
    ArrayList<Replies> getReplies(Integer commentId);
}
