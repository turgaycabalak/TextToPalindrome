package com.palindrome.TextToPalindrome.genericResponse;
public class SuccessDataResult<T> extends DataResult {
    public SuccessDataResult(T data) {
        super(data, true);
    }

    public SuccessDataResult(T data, String message) {
        super(data, true, message);
    }

    public SuccessDataResult() {
        super(null, true);
    }
}
