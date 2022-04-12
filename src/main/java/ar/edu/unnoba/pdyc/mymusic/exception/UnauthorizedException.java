package ar.edu.unnoba.pdyc.mymusic.exception;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(){}

    public UnauthorizedException(String message){
        super(message);
    }

}
