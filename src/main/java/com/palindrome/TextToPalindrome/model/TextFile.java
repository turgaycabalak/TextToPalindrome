package com.palindrome.TextToPalindrome.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@NoArgsConstructor
public class TextFile {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String textContent;
    private boolean palindromeForIgnoreCase;
    private boolean palindromeForNotIgnoreCase;

    public TextFile(String textContent, boolean palindromeForIgnoreCase, boolean palindromeForNotIgnoreCase) {
        this.textContent = textContent;
        this.palindromeForIgnoreCase = palindromeForIgnoreCase;
        this.palindromeForNotIgnoreCase = palindromeForNotIgnoreCase;
    }

}
