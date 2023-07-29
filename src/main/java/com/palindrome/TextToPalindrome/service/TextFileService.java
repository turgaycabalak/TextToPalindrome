package com.palindrome.TextToPalindrome.service;

import com.palindrome.TextToPalindrome.controller.TextFileMapper;
import com.palindrome.TextToPalindrome.dto.TextFileResponseDto;
import com.palindrome.TextToPalindrome.dto.TextSaveRequestDto;
import com.palindrome.TextToPalindrome.exceptions.NotFoundException;
import com.palindrome.TextToPalindrome.model.TextFile;
import com.palindrome.TextToPalindrome.repository.TextFileRepository;
import com.palindrome.TextToPalindrome.repository.specification.CustomTextFileRepository;
import com.palindrome.TextToPalindrome.repository.specification.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextFileService {

    private final TextFileRepository textFileRepository;
    private final TextFileMapper textFileMapper;
    private final CustomTextFileRepository customTextFileRepository;

    private TextFile getByIdOrThrowNotFound(String textId) {
        return textFileRepository.findById(textId)
                .orElseThrow(() -> new NotFoundException(String.format("Text not found by ID: %s", textId)));
    }

    private boolean checkIfExistsByTextContent(String text) {
        return textFileRepository.existsByTextContent(text);
    }

    private boolean nullCheckForText(String text) {
        return Objects.isNull(text) || text.isEmpty();
    }

    private boolean checkIfPalindrome(String textContent, boolean ignoreCase) {
        if (nullCheckForText(textContent)) return false;

        String cleanedText = ignoreCase ?
                textContent.replaceAll("[^a-zA-Z0-9]", "").toLowerCase() :
                textContent.replaceAll("\\s", "");

        return cleanedText.contentEquals(new StringBuilder(cleanedText).reverse());
    }


    @Transactional
    public boolean saveAndCheckPalindrome(TextSaveRequestDto requestDto) {
        boolean res = checkIfExistsByTextContent(requestDto.textContent());
        boolean ignoreCaseResult = checkIfPalindrome(requestDto.textContent(), true);
        boolean notIgnoreCaseResult = checkIfPalindrome(requestDto.textContent(), false);

        if (!res) {
            textFileRepository.save(new TextFile(requestDto.textContent(), ignoreCaseResult, notIgnoreCaseResult));
        }
        return requestDto.ignoreCase() ? ignoreCaseResult : notIgnoreCaseResult;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Page<TextFileResponseDto> getAllTextsByPagination(Pageable pageable) {
        Page<TextFile> textFilesPage = textFileRepository.findAll(pageable);
        List<TextFileResponseDto> listResponseDto = textFileMapper.toListResponseDto(textFilesPage.getContent());
        return new PageImpl<>(listResponseDto, textFilesPage.getPageable(), textFilesPage.getTotalElements());
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public TextFileResponseDto getTextFileDtoById(String textId) {
        TextFile textFromDb = getByIdOrThrowNotFound(textId);
        return textFileMapper.toResponseDto(textFromDb);
    }

    @Transactional
    public boolean updateTextById(String textId, TextSaveRequestDto requestDto) {
        TextFile textFromDb = getByIdOrThrowNotFound(textId);

        boolean ignoreCaseResult = checkIfPalindrome(requestDto.textContent(), true);
        boolean notIgnoreCaseResult = checkIfPalindrome(requestDto.textContent(), false);
        textFromDb.setTextContent(requestDto.textContent());
        textFromDb.setPalindromeForIgnoreCase(ignoreCaseResult);
        textFromDb.setPalindromeForNotIgnoreCase(notIgnoreCaseResult);
        textFileRepository.save(textFromDb);

        return requestDto.ignoreCase() ? ignoreCaseResult : notIgnoreCaseResult;
    }

    @Transactional
    public void deleteById(String textId) {
        boolean exists = textFileRepository.existsById(textId);
        if (!exists) throw new NotFoundException("Text not found by ID: " + textId);

        textFileRepository.deleteById(textId);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<TextFileResponseDto> searchByFilter(List<Filter> filters) {
        List<TextFile> queryResult = customTextFileRepository.getQueryResult(filters);
        return textFileMapper.toListResponseDto(queryResult);
    }
}
