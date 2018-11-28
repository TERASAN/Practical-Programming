package newlang3;

import com.sun.jdi.DoubleValue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.HashMap;

import static newlang3.ValueType.DOUBLE;
import static newlang3.ValueType.INTEGER;
import static newlang3.ValueType.STRING;

public class LexicalAnalyzerImpl implements LexicalAnalyzer {
    PushbackReader reader;
    static HashMap<String, LexicalUnit> Reserved_words = new HashMap<>();
    static HashMap<String, LexicalUnit> Symbols = new HashMap<>();

    public LexicalAnalyzerImpl(InputStream in){
        Reader ir = new InputStreamReader(in);
        reader = new PushbackReader(ir);
    }

    @Override
    public LexicalUnit get() throws Exception {
        while (true) {
            int ci = reader.read();//1文字読む

            //  ファイル終わり
            if (ci < 0) {//ファイルが終わると-1を返す
                return new LexicalUnit(LexicalType.EOF);//終了
            }else{
                char c =(char)ci;

                if((c == ' ') || (c == '\t')){
                    continue;
                }
            }

            char c = (char) ci;//int型をchar型に変換（文字コードで返ってくる）


            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {// アルファベットの時
                reader.unread(ci);
                return getString();
            }else if(Symbols.containsKey(c + "")){//char型をString型に変える神業　記号の時
                reader.unread(ci);
                return getSymbol();
            }else if((c >='0' && c <= '9')){//数字のとき
                reader.unread(ci);
                return getNumber();
            }else if(c == '"'){//リテラル（"で囲まれたエリア）のとき
                reader.unread(ci);
                return getLiteral();
            }
        }
    }


    private LexicalUnit getString() throws Exception {
        String target = "";//とりあえず空白のStringを作成

        while (true) {
            int ci = reader.read();//1文字読む
            if(ci < 0) break;//ファイルが終わってたらwhileを抜ける
            char c = (char) ci;//変換してるだけ

            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                target += c;//後ろにくっつける
                continue;
            }
            reader.unread(ci);
            break;
        }
        if(Reserved_words.containsKey(target)){
            return Reserved_words.get(target);//containsKeyで予約語の中に同じものがないかを調べ、あればそのKeyの値を持ってくる
        }else{
            return new LexicalUnit(LexicalType.NAME, new ValueImpl(target, STRING));

        }
    }

    private LexicalUnit getSymbol() throws Exception{
        String target ="";
         while(true){
             int ci = reader.read();//1文字読む
             if(ci < 0) break;//ファイルが終わってたらwhileを抜ける
             char c = (char) ci;//変換してるだけ

             if (Symbols.containsKey(target+c)) {
                 target += c;//後ろにくっつける
                 continue;
             }
             reader.unread(ci);
             break;
         }
         return Symbols.get(target);
    }


    private LexicalUnit getNumber() throws Exception {
        String target = "";
        boolean pointFlag = false;
        while (true) {
            int ci = reader.read();
            if (ci < 0) break;//ファイルが終わってたらwhileを抜ける
            char c = (char) ci;//変換してるだけ

            if ((c >='0' && c <= '9')) {
                target += c;//後ろにくっつける
                continue;
            }else if(c =='.'){
                target += c;
                pointFlag = true;
                continue;
            }else if(c == '.' && pointFlag){
                throw new Exception("小数点が多いよ！");
            }
            reader.unread(ci);
            break;
        }
        if(pointFlag){
            return new LexicalUnit(LexicalType.DOUBLEVAL,new ValueImpl(target, DOUBLE));
        }else{
            return new LexicalUnit(LexicalType.INTVAL,new ValueImpl(target, INTEGER));
        }
    }

    private LexicalUnit getLiteral() throws Exception{
        String target = "";
        reader.read();
        while(true){
            int ci = reader.read();
            if (ci < 0) throw new Exception("\"がないよ！");//ファイルが終わってたらエラー文を表示
            char c = (char) ci;//変換してるだけ
            if(c !='"'){
                target += c;
                continue;
            }else{
                break;
            }
        }
        return new LexicalUnit(LexicalType.LITERAL,new ValueImpl(target,STRING));
    }

    static {//staticはclassが最初にインスタンス化されたときのみ実行される(重複防止)
        //作成したMapにput(値をいれる)する。 map.put("Key",Value)
        Reserved_words.put("IF", new LexicalUnit(LexicalType.IF));
        Reserved_words.put("THEN", new LexicalUnit(LexicalType.THEN));
        Reserved_words.put("ELSE", new LexicalUnit(LexicalType.ELSE));
        Reserved_words.put("ELSEIF", new LexicalUnit(LexicalType.ELSEIF));
        Reserved_words.put("FOR", new LexicalUnit(LexicalType.FOR));
        Reserved_words.put("FORALL", new LexicalUnit(LexicalType.FORALL));
        Reserved_words.put("NEXT", new LexicalUnit(LexicalType.NEXT));
        Reserved_words.put("SUB", new LexicalUnit(LexicalType.FUNC));
        Reserved_words.put("DIM", new LexicalUnit(LexicalType.DIM));
        Reserved_words.put("AS", new LexicalUnit(LexicalType.AS));
        Reserved_words.put("END", new LexicalUnit(LexicalType.END));
        Reserved_words.put("WHILE", new LexicalUnit(LexicalType.WHILE));
        Reserved_words.put("DO", new LexicalUnit(LexicalType.DO));
        Reserved_words.put("UNTIL", new LexicalUnit(LexicalType.UNTIL));
        Reserved_words.put("LOOP", new LexicalUnit(LexicalType.LOOP));
        Reserved_words.put("TO", new LexicalUnit(LexicalType.TO));
        Reserved_words.put("WEND", new LexicalUnit(LexicalType.WEND));
        Reserved_words.put("DO", new LexicalUnit(LexicalType.DO));
        Symbols.put("=", new LexicalUnit(LexicalType.EQ));
        Symbols.put("<", new LexicalUnit(LexicalType.LT));
        Symbols.put(">", new LexicalUnit(LexicalType.GT));
        Symbols.put("<=", new LexicalUnit(LexicalType.LE));
        Symbols.put("=<", new LexicalUnit(LexicalType.LE));
        Symbols.put(">=", new LexicalUnit(LexicalType.GE));
        Symbols.put("=>", new LexicalUnit(LexicalType.GE));
        Symbols.put("<>", new LexicalUnit(LexicalType.NE));
        Symbols.put(".", new LexicalUnit(LexicalType.DOT));
        Symbols.put("+", new LexicalUnit(LexicalType.ADD));
        Symbols.put("-", new LexicalUnit(LexicalType.SUB));
        Symbols.put("*", new LexicalUnit(LexicalType.MUL));
        Symbols.put("/", new LexicalUnit(LexicalType.DIV));
        Symbols.put(")", new LexicalUnit(LexicalType.LP));
        Symbols.put("(", new LexicalUnit(LexicalType.RP));
        Symbols.put(",", new LexicalUnit(LexicalType.COMMA));
        Symbols.put("\n", new LexicalUnit(LexicalType.NL));

    }



    @Override
    public boolean expect(LexicalType type) throws Exception {
        return false;
    }

    @Override
    public void unget(LexicalUnit token) throws Exception {

    }
}
