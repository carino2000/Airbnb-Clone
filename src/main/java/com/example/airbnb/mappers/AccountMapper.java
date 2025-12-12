package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Account;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface AccountMapper {
    int insertOne(Account account);
    int countDuplicateId (String id);
    int countDuplicateEmail (String email);
    Account selectById(String id);
    int updateAccountInfo(Account account);
    int deleteAccountById(String id);
    int updateAccountPw(Map<String, String> map);
}
