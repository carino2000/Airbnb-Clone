package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageMapper {
    Message selectLastMessageByCode(String reservationCode);

    List<Message> selectMessageByCode(String reservationCode);

    int countMessageReadFlagByCode(Map<String, String> map);

    int updateMessageReadFlagById(int id);

    // 쪽지 작성
    int insertMessage(Message message);

    // 쪽지 조회
    List<Message> selectMessage(String recipientId);

    // 쪽지 삭제
    int deleteMessage(int messageId);


}
