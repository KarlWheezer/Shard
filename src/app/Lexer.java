package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static app.Token.Type;

public class Lexer {
   public String filename, code; private char cur;
   private int line, col, index;
   public ArrayList<Token> tokens;

   public Lexer(String filename) throws FileNotFoundException, IOException {
      this.code = Lexer.reader(filename);
      this.index = 0; this.line = 1; this.col = 1;
      this.filename = filename; 
      this.cur = this.code.toCharArray()[0];
      this.tokens = new ArrayList<Token>();
   }

   private static String reader(String filename) throws FileNotFoundException, IOException {
      StringBuilder resultStringBuilder = new StringBuilder();
      try (BufferedReader br
         = new BufferedReader(new FileReader(new File(filename)))) {
         String line;
         while ((line = br.readLine()) != null) {
               resultStringBuilder.append(line).append("\n");
         }
      }
      return resultStringBuilder.toString();
   }
   
   private void next() { this.next(1); }
   private void next(int steps) {
      index += steps; col += steps;
      cur = code.toCharArray()[index];
   }

   private void push(Token.Type type, String value) {
      push(type, value, this.col);
   }
   private void push(Token.Type type, String value, int col) {
      tokens.add(new Token(type, value, line, col));
   }

   private void find_tokens() {
      if (Character.isAlphabetic(cur)) { int col = this.col; String buf = "";
         while (Character.isLetterOrDigit(cur) || cur == '_') { buf += cur; next(); }
         switch (buf) {
            case "fun": case "if": case "else": case "set": case "var":
               push(Type.keywd, buf, col); break;
            default:
               push(Type.ident, buf, col); break;
         }
      } if (Character.isDigit(cur)) { int col = this.col; String buf = "";
         while (Character.isDigit(cur) || cur == '_' || cur == '.') { buf += cur; next(); }
         push(Type.numbr, buf, col);
      } if (cur == '"') { int col = this.col; String buf = ""; next();
         while (cur != '"') {
            if (cur == '\\') { buf += cur; next(); }
            if (index >= code.length()) { System.err.println(String.format(
               "Error: end of file reached without string closing char '\"' --> %s[%d:%d]",
               filename, line, col
            )); System.exit(1); }
            buf += cur; next();
         } next(); push(Type.strng, buf, col);
      } else switch (cur) {
         case '[': push(Type.lBrac, Character.toString(cur)); next(); break;
         case '{': push(Type.lBrak, Character.toString(cur)); next(); break;
         case '(': push(Type.lParn, Character.toString(cur)); next(); break;
         case ']': push(Type.rBrac, Character.toString(cur)); next(); break;
         case '}': push(Type.rBrak, Character.toString(cur)); next(); break;
         case ')': push(Type.rParn, Character.toString(cur)); next(); break;

         case '\r': next(); this.col -= 1;                 break;
         case '\n': next(); this.col = 1; this.line += 1;  break;
         case '\0': next(); push(Type._eof_, "\0"); break;
         case ' ':  next();                                break;
         case ';':  push(Type.semi_, ";"); next();  break;
         case ':':  push(Type.colon, ":"); next();  break;
         case ',':  push(Type.comma, ","); next();  break;

         case '=':
            if (code.toCharArray()[index + 1] == '>')
               { push(Type.arrow, "=>"); next(2); break; }
            if (code.toCharArray()[index + 1] == '=')
               { push(Type.compr, "=="); next(2); break; }
            else
               { push(Type.equal, "=");  next(1); break; }

         case '>': case '<':
            if (code.toCharArray()[index + 1] == '=')
               { push(Type.compr, Character.toString(cur)+"="); next(2); break; }
            else
               { push(Type.compr, Character.toString(cur)); next(); break;}
         
         default: System.out.println(String.format(
            "error: unrecognised char '%c' --> %s[%d:%d]",
            cur, filename, line, col
         )); next();
      }
   }

   public ArrayList<Token> tokenize() {
      while (index < code.length() -1)
         this.find_tokens();
      push(Type._eof_, "\0");

      return this.tokens;
   }
}