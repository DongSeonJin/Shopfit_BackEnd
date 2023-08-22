package com.spring.community.service;

import com.spring.community.entity.Post;
import com.spring.community.entity.Reply;
//import com.spring.community.repository.PostRepository;
import com.spring.community.repository.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReplyServiceTest {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    ReplyService replyService;

    @Test
    @Transactional
    public void findAllByPostIdTest() {
        // given
        long postId = 1;

        // when
        List<Reply> replies = replyService.findAllByPostId(postId);

        // then
        assertEquals(2, replies.size());
    }

    @Test
    @Transactional
    public void findByReplyIdTest() {
        // given
        long replyId = 16;
        String content = "첫 번째 댓글";

        // when
        Reply reply = replyService.findByReplyId(replyId);

        // then
        assertEquals(replyId, reply.getReplyId());
        assertEquals(content, reply.getContent());
    }

    @Test
    @Transactional
    public void  deleteByReplyIdTest() {
        // given
        long replyId = 17;
        long postId = 1;

        // when
        replyService.deleteByReplyId(replyId);

        // then
        assertEquals(1, replyService.findAllByPostId(postId).size());
//        assertNull(replyService.findByReplyId(replyId));
    }

    @Test
    @Transactional
    public void saveTest() {
        // given
        long postId = 1;
        String content = "test content";

        // Post 객체 생성 및 저장
        Post post = new Post();
        post.setPostId(postId);

        Reply reply = Reply.builder()
                .content(content)
                .post(post)
                .build();

        // when
        replyService.save(reply);

        // then
        List<Reply> replyList = replyService.findAllByPostId(postId);
        assertEquals(3, replyList.size());
    }

    @Test
    @Transactional
    public void updateTest() {
        // given
        long replyId = 16;
        String content = "update content";

        Reply reply = new Reply();
        reply.setReplyId(replyId);
        reply.setContent(content);

        // when
        replyService.update(reply);

        // then
        Reply updatedReply = replyRepository.findById(replyId).orElse(null);
        assertNotNull(updatedReply);
        assertEquals(content, updatedReply.getContent());

    }




}
