import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Lexer {
    String code; Integer index; Integer col;
    String filename; Integer line; List<Token> tokens;
    List<Character> chars; Character cur;

    public Lexer(String filename) throws IOException {
        this.filename = filename; this.line = 1; this.col  = 1; this.index = 0;
        this.code = Files.readString(Path.of(this.filename)); this.chars = toChar();
        this.cur = this.chars.get(this.index).charValue(); this.tokens = new ArrayList<>();
    }
    private void next() { this.index += 1; this.col += 1; cur = this.chars.get(this.index).charValue(); }

    private void push(Type type, String value, Integer col) {
        this.tokens.add(new Token(type, value, this.line, col));
    }

    private void match() { Integer col_ = this.col;
        if (Character.isLetter(this.cur)) { String bfr = "";
            while (Character.isLetterOrDigit(this.cur) || this.cur == '_') { bfr += this.cur; next(); }
            this.push(Type.ident, bfr, col_);
        } else if (Character.isDigit(this.cur)) { String bfr = "";
            while (Character.isDigit(this.cur) || this.cur == '.') { bfr += this.cur; next(); }
            this.push(Type.num, bfr, col_);
        } else if (this.cur == '"') { String bfr = ""; next();
            while (this.cur != '"') {
                if (this.cur == '\\') { bfr += this.cur; next(); }
                bfr += this.cur; next();
                if (this.index >= this.chars.size()) { System.out.println("End of file reached without string closing characture '\"': "+filename+"["+line+":"+col+"]"); System.exit(1); }
            } next(); push(Type.str, bfr, col_);
        } else switch (this.cur.toString()) {
            case "[": push(Type.lBrac, this.cur.toString(), col_); next(); break;
            case "{": push(Type.lBrak, this.cur.toString(), col_); next(); break;
            case "(": push(Type.lParn, this.cur.toString(), col_); next(); break;

            case "]": push(Type.rBrac, this.cur.toString(), col_); next(); break;
            case "}": push(Type.rBrak, this.cur.toString(), col_); next(); break;
            case ")": push(Type.rParn, this.cur.toString(), col_); next(); break;

            case ".": push(Type.dot, this.cur.toString(), col_);  next(); break;
            case ";": push(Type.semi, this.cur.toString(), col_); next(); break;
            case ",": push(Type.comm, this.cur.toString(), col_); next(); break;

            case " ": next(); break;
            case "\r": next(); col -= 1; break;
            case "\n": next(); col = 1; line ++; break;

            case "=": 
                if (this.chars.get(this.index + 1) == '>') {
                    push(Type.arr, "=>", col_); next(); next(); break;
                } else if (this.chars.get(this.index + 1) == '=') {
                    push(Type.comp, "==", col_); next(); next(); break;
                } else { push(Type.equ, this.cur.toString(), col); next(); break; }
            default:
                System.out.println("Unknown characture '"+cur+"': "+filename+"["+line+":"+col+"]"); next(); 
        }
    }

    public void tokenize() {
        while (this.index < this.chars.size() - 1) { match(); }
        for (Token tok : this.tokens) {
            System.out.println(tok.toString());
        }
    } 

    private List<Character> toChar() {
        List<Character> chars = new ArrayList<>();
        for( char ch : this.code.toCharArray()) { chars.add(ch); }

        return chars;
    }
}
