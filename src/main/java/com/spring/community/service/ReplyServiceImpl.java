package com.spring.community.service;

import com.spring.community.entity.Reply;
import com.spring.community.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    @Autowired
    public ReplyServiceImpl (ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }


    @Override
    public List<Reply> findAllByPostId(long postId) {
        return replyRepository.findAllByPost_PostId(postId);
    }

    @Override
    public Reply findByReplyId(long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
    }

    @Override
    public void deleteByReplyId(long replyId) {
        replyRepository.deleteById(replyId);
    }

    @Override
    public void save(Reply reply) {
        replyRepository.save(reply);
    }

    @Override
    public void update(Reply reply) {
        Reply existingReply = findByReplyId(reply.getReplyId());
        existingReply.setContent(reply.getContent());
        existingReply.setUpdatedAt(reply.getUpdatedAt());
        replyRepository.save(existingReply);

    }
}
