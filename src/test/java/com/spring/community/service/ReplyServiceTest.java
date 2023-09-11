package com.spring.community.service;

import com.spring.community.entity.Post;
import com.spring.community.entity.Reply;
import com.spring.community.repository.ReplyRepository;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("3번 글에 달린 댓글 개수 1개")
    public void findAllByPostIdTest() {
        // given
        long postId = 3L;

        // when
        List<Reply> replies = replyService.findAllByPostId(postId);

        // then
        assertEquals(1, replies.size());
    }

    @Test
    @Transactional
    @DisplayName("댓글번호 3번의 댓글 내용은 'reply content3'")
    public void findByReplyIdTest() {
        // given
        Long replyId = 3L;
        String content = "reply content3";

        // when
        Reply reply = replyService.findByReplyId(replyId);

        // then
        assertEquals(replyId, reply.getReplyId());
        assertEquals(content, reply.getContent());
    }

    @Test
    @Transactional
    @DisplayName("뎃글번호 2번을 삭제하면 1번 글의 댓글 개수는 1개")
    public void  deleteByReplyIdTest() {
        // given
        Long replyId = 2L;
        Long postId = 1L;

        // when
        replyService.deleteByReplyId(replyId);

        // then
        assertEquals(1, replyService.findAllByPostId(postId).size());
    }

    @Test
    @Transactional
    @DisplayName("3번 글에 댓글을 등록하면 댓글 개수는 2개가 될 것이다.")
    public void saveTest() {
        // given
        long postId = 3L;
        String content = "test reply content";

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
        assertEquals(2, replyList.size());
    }

    @Test
    @Transactional
    @DisplayName("댓글번호 3번의 내용을 'update reply content' 로 수정하고 조회")
    public void updateTest() {
        // given
        long replyId = 3;
        String content = "update reply content";

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
