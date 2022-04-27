public enum Type {
    ident("identifier"),
    key("keyword"),
    str("string"),
    num("number"),

    lBrac("left brace"),
    lBrak("left bracket"),
    lParn("left paren"),

    rBrac("right brace"),
    rBrak("right bracket"),
    rParn("right paren"),

    arr("arrow"),
    dot("dot"),
    equ("equal"),
    comm("comma"),
    semi("semi colon"),

    comp("compare"),
    oper("operator"),

    EOF("end of file");
    private String _a;
    Type(String str) { this._a = str; };
    @Override
    public String toString() { return this._a; }
}