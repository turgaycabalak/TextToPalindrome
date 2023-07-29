package com.palindrome.TextToPalindrome.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TextSaveRequestDto(

        @NotNull(message = "{constraints.TextSaveRequestDto.TextNotNull}")
        @Size(min = 3, message = "{constraints.TextSaveRequestDto.TextSize}")
        String textContent,

        /**
         *  Case sensitive of the textContent.
         *  ignoreCase: true => aba returns true, abA returns true.
         *  ignoreCase: false => aba returns true, abA returns false.
         */
        boolean ignoreCase
) {
}
