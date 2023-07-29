package com.palindrome.TextToPalindrome.controller;

import com.palindrome.TextToPalindrome.dto.TextFileResponseDto;
import com.palindrome.TextToPalindrome.model.TextFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TextFileMapper {

    @Mapping(source = "id", target = "textId")
    TextFileResponseDto toResponseDto(TextFile textFile);

    List<TextFileResponseDto> toListResponseDto(List<TextFile> textFiles);
}
