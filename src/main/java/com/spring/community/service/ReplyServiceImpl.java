package com.spring.community.service;

import com.spring.community.entity.Reply;
import com.spring.community.repository.ReplyRepository;
import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
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
        List<Reply> replyList = replyRepository.findAllByPost_PostId(postId);

        return replyList;
    }

    @Override
    public Reply findByReplyId(long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new CustomException(ExceptionCode.REPLY_NOT_FOUND)); // enum 활용 예시
    }

    @Override
    public void deleteByReplyId(long replyId) {
        if (!replyRepository.existsById(replyId)) {
            throw new CustomException(ExceptionCode.REPLY_NOT_FOUND);
        }
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
