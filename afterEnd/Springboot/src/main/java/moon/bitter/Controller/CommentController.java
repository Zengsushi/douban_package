package moon.bitter.Controller;

import moon.bitter.Pojo.Comments;
import moon.bitter.Pojo.Replies;
import moon.bitter.Service.CommentService;
import moon.bitter.Utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 发布评论
    @PostMapping("/addComments")
    public Result addComments(@RequestBody Comments comments) {
        return commentService.addComments(comments);
    }

    // 获取评论
    @PostMapping("/getComments")
    public Result getComments(@RequestBody Comments comments) {
        return commentService.getComments(comments);
    }

    // 添加回复
    @PostMapping("addReplies")
    public Result addReplies(@RequestBody Replies replies) {
        return commentService.addReplies(replies);
    }

    // 删除评论
    @PostMapping("deleteComments")
    public Result deleteComments(@RequestBody Comments comments) {
        return commentService.deleteComments(comments.getId());
    }

    // 删除回复
    @PostMapping("deleteReplies")
    public Result deleteReplies(@RequestBody Replies replies) {
        return commentService.deleteReplies(replies);
    }

    // 获取回复
    @PostMapping("getReplies")
    public Result getReplies(@RequestBody Replies replies) {
        return commentService.getReplies(replies);
    }


}
