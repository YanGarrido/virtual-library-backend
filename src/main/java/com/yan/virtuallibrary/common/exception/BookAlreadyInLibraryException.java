package com.yan.virtuallibrary.common.exception;

public class BookAlreadyInLibraryException extends RuntimeException {

    public BookAlreadyInLibraryException() {super("This book is already in your library!");}
    public BookAlreadyInLibraryException(String message) {
        super(message);
    }
}
