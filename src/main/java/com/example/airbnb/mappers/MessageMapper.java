package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {
    int insertOne(Message message);
}
