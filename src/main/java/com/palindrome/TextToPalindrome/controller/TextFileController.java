package com.palindrome.TextToPalindrome.controller;

import com.palindrome.TextToPalindrome.dto.TextFileResponseDto;
import com.palindrome.TextToPalindrome.dto.TextSaveRequestDto;
import com.palindrome.TextToPalindrome.genericResponse.SuccessDataResult;
import com.palindrome.TextToPalindrome.genericResponse.SuccessResult;
import com.palindrome.TextToPalindrome.repository.specification.Filter;
import com.palindrome.TextToPalindrome.service.TextFileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/text")
public class TextFileController {

    private final TextFileService textFileService;

    
    @PostMapping
    public ResponseEntity<SuccessDataResult<Boolean>> saveAndCheckPalindrome(@Valid @RequestBody TextSaveRequestDto requestDto) {
        boolean palindromeResult = textFileService.saveAndCheckPalindrome(requestDto);
        return ResponseEntity.ok(new SuccessDataResult<>(palindromeResult,
                "Text saved and palindrome result returned successfully."));
    }

    @GetMapping("/slice")
    public ResponseEntity<SuccessDataResult<Page<TextFileResponseDto>>> getAllTextsByPagination(Pageable pageable) {
        Page<TextFileResponseDto> allTextsByPagination = textFileService.getAllTextsByPagination(pageable);
        String message = String.format("All texts are listed by page: %s, and by page size: %s", pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(new SuccessDataResult<>(allTextsByPagination, message));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessDataResult<TextFileResponseDto>> getTextFileDtoById(@PathVariable("id") String textId) {
        return ResponseEntity.ok(new SuccessDataResult<>(textFileService.getTextFileDtoById(textId), "Text found by ID: " + textId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessDataResult<Boolean>> updateTextById(@PathVariable("id") String textId,
                                                                     @Valid @RequestBody TextSaveRequestDto requestDto) {
        return ResponseEntity.ok(new SuccessDataResult<>(textFileService.updateTextById(textId, requestDto),
                String.format("Text updated successfully by ID: %s", textId)));
    }

    @DeleteMapping
    public ResponseEntity<SuccessResult> deleteById(@RequestParam("id") String textId) {
        textFileService.deleteById(textId);
        return ResponseEntity.ok(new SuccessResult(String.format("Text deleted successfully by ID: %s", textId)));
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessDataResult<List<TextFileResponseDto>>> searchByFilter(@RequestBody List<Filter> filters) {
        List<TextFileResponseDto> searchByFilter = textFileService.searchByFilter(filters);
        return ResponseEntity.ok(new SuccessDataResult<>(searchByFilter, "Texts found by filter"));
    }


}
