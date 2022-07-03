package app;

import java.io.FileNotFoundException;
import java.io.IOException;

import app.Token.Type;

public class Main {
   public static void main(String[] args) {
      Lexer lexer; Parser parser;

      try {
         parser = new Parser(new Lexer("src/test/main.baf"));
         parser.eat("token", Type.ident, Type.rParn);
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}