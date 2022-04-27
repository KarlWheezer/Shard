import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer("main.baf");
        lexer.tokenize();
    }
}