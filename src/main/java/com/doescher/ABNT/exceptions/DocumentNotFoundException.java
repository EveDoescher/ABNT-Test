package com.doescher.ABNT.exceptions;

public class DocumentNotFoundException extends RuntimeException{
    public DocumentNotFoundException(Long id){
        super("Documento com o ID " + id + " não foi encontrado");
    }
}
