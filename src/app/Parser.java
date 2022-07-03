package app;

import java.util.ArrayList;

import static app.Token.Type;

public class Parser {
   private Lexer lexer;
   private ArrayList<Token> tokens;
   private Token prev, cur, next;
   private int index = 0, errors = 0;

   public Parser(Lexer lexer) {
      this.lexer = lexer;
      this.tokens = this.lexer.tokenize();
      
      this.prev = tokens.get(index);
      this.cur  = this.prev;
      this.next = tokens.get(index + 1);
   }

   private void error(String unexpected, String value, Type... types) {
      String buf = ""; int i=0; String[] msg = new String[3];

      while (i < cur.col-1) { buf += " "; i ++; } i = 0; buf += "^";
      while (i < cur.value.length() -1) { buf += "~"; i ++; }

      msg[0] = String.format(
         "Error: unexpected %s => %s[%d:%d]", unexpected, lexer.filename, cur.line, cur.col
      ); msg[1] = String.format(
         "%d | %s", cur.line, lexer.code.split("\n")[cur.line - 1]
      ); msg[2] = String.format(
         "%s | %s unexpected %s, wanted", 
         String.valueOf(cur.line).replaceAll("[0-9]", " "),
         buf, cur.type.str
      ); buf = "";

      for (i=0; i < types.length; i ++) buf += types[i].str + ", ";
      msg[2] += " [" + buf.substring(0, buf.length()-2) + "]";

      if (value != null) msg[2] += " with value \"" + value + "\"";

      for (String str: msg) System.out.println(str); errors += 1;
   }

   private void advance(int steps) { index += steps;
      prev = tokens.get(index -1);
      cur  = tokens.get(index   );
      next = tokens.get(index +1);
   }

   public Token eat(String value, Type... types) {
      if (cur.type == Type._eof_)
         { error("end of file", value, types); System.exit(1); return prev; }
      
      if (value != null)
         if (value != cur.value) 
            { error("token", value, types); advance(1); return prev;}
      
      for(Type type: types)
         if (type == cur.type) { advance(1); return prev; }
      
      errors += 1; error("token", value, types); return prev;
   }
}