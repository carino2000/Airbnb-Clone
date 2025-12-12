package com.example.airbnb.controller;

import com.example.airbnb.mappers.LikesMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodation")
public class LikesController {
    final LikesMapper likesMapper;

    //좋아요 조회
    @GetMapping("/accommodations/likes")
    public String getAccommodationLikes() {
        return "좋아요 조회";
    }

    //좋아요 등록
    @PostMapping("/accommodations/{accommodationId}/likes")
    public String addAccommodationLike() {
        return "좋아요 등록";
    }



}
