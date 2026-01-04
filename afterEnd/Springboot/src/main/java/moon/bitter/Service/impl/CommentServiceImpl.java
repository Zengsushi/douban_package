package moon.bitter.Service.impl;

import moon.bitter.Mapper.CommentMapper;
import moon.bitter.Mapper.UserMapper;
import moon.bitter.Pojo.Comments;
import moon.bitter.Pojo.Replies;
import moon.bitter.Service.CommentService;
import moon.bitter.Utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    // 评论
    @Override
    public Result addComments(Comments comments) {
        try {
            commentMapper.addComments(comments);
            return Result.success();
        } catch (Exception e) {
            return Result.error("评论失败");
        }
    }

    @Override
    public Result getComments(Comments comments) {
        // 评论列表
        ArrayList<Comments> c = commentMapper.getCommentsList(comments);
        for (Comments comment : c) {
            String nickName = userMapper.getUserInfo(comment.getUserId());
            comment.setNickname(nickName);
        }
        return Result.success(c);
    }

    @Override
    public Result addReplies(Replies replies) {
        try {
            commentMapper.addRepliesCount(replies);
        } catch (RuntimeException e) {
            return Result.error("评论失败");
        }
        Comments comments = commentMapper.getComments(replies.getCommentId());
        comments.setReCount(comments.getReCount() + 1);
        commentMapper.updateCommentsCount(comments);
        return Result.success();
    }

    @Override
    public Result deleteReplies(Replies replies) {
        try {
            commentMapper.deleteReplies(replies.getId());
        } catch (RuntimeException e) {
            return Result.error("评论删除失败");
        }
        Comments comments = commentMapper.getComments(replies.getCommentId());
        comments.setReCount(comments.getReCount() - 1);
        commentMapper.updateCommentsCount(comments);
        return null;
    }

    @Override
    public Result deleteComments(String id) {

        try {
            commentMapper.deleteComments(id);
            commentMapper.initReplies(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("评论删除失败");
        }
    }

    @Override
    public Result getReplies(Replies replies) {
        try {
            ArrayList<Replies> list = commentMapper.getReplies(replies.getCommentId());
            for (Replies reply : list) {
                String nickName = userMapper.getUserInfo(reply.getUserId());
                reply.setNickname(nickName);
            }
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("服务器响应异常");
        }
    }
}
