package ro.msg.learning.shop.exceptions;


import lombok.Data;

@Data
public class FileTypeMismatchException extends RuntimeException {


    private final String context;

    public FileTypeMismatchException(String context) {

        super("FileTypeMismatchException");
        this.context = context;

    }
}
