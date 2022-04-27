public class Token {
    private String value; private Type type;
    private Integer line; private Integer col;

    public Token(Type type, String value, Integer line, Integer col) {
        this.value = value; this.type = type; this.line = line; this.col = col;
    }

    @Override
    public String toString() {
        return "Token { type: "+type.toString()+", value: \""+value+"\", line: "+line+", col: "+col+" };";
    }
}