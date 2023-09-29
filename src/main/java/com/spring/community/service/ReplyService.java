package com.spring.community.service;


import com.spring.community.entity.Reply;

import java.util.List;

public interface ReplyService {

    List<Reply> findAllByPostId(long postId);

    Reply findByReplyId (long replyId);

    void deleteByReplyId (long replyId);

    void save(Reply reply);

    void update (Reply reply);

}
