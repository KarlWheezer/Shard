package app;

public class Token {
   public static enum Type {
      ident("identifier"), keywd("keyword"), strng("string"), numbr("number"),

      lBrac("left-brace"),  lBrak("left-bracket"),  lParn("left-paren"),
      rBrac("right-brace"), rBrak("right-bracket"), rParn("right-paren"),

      _dot_("dot"), semi_("semi-colon"), colon("colon"), arrow("arrow"), comma("comma"),
      equal("equals"), compr("compare"), opert("operator"), _eof_("end-of-file");

      public String str; 
      private Type(String s) { this.str = s; }
   }

   public Type type;
   public String value;
   public int line, col;

   public Token(Type type, String value, int line, int col) {
      this.type = type; this.value = value;
      this.line = line; this.col = col;
   }

   @Override
   public String toString() { return String.format(
      "token { type=%s, value=\"%s\", line=%d, col=%d };",
      type.str, value, line, col
   );  }
}