package com.palindrome.TextToPalindrome.dto;

public record TextFileResponseDto(
        String textId,
        String textContent,
        boolean palindromeForIgnoreCase,
        boolean palindromeForNotIgnoreCase
) {
}
