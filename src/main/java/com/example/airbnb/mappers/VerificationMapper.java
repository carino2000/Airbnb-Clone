package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Verification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VerificationMapper {
    int insertOne(Verification verification);

    Verification selectLatestByEmail(String email);
}
