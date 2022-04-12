package ar.edu.unnoba.pdyc.mymusic.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
