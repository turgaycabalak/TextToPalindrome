package com.palindrome.TextToPalindrome.repository;

import com.palindrome.TextToPalindrome.model.TextFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TextFileRepository extends JpaRepository<TextFile, String>, JpaSpecificationExecutor<TextFile> {
    boolean existsByTextContent(String text);
}
