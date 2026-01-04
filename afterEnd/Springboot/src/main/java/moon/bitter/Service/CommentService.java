package moon.bitter.Service;

import moon.bitter.Pojo.Comments;
import moon.bitter.Pojo.Replies;
import moon.bitter.Utils.Result;

public interface CommentService {
    Result addComments(Comments comments);

    Result getComments(Comments comments);

    Result addReplies(Replies replies);

    Result deleteReplies(Replies replies);

    Result deleteComments(String id);

    Result getReplies(Replies replies);
}
