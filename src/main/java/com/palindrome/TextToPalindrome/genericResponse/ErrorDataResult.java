package com.palindrome.TextToPalindrome.genericResponse;
public class ErrorDataResult<T> extends DataResult {
    public ErrorDataResult(T data) {
        super(data, false);
    }

    public ErrorDataResult(T data, String message) {
        super(data, false, message);
    }

    public ErrorDataResult() {
        super(null, false);
    }
}
