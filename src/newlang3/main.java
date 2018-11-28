package newlang3;

import java.io.*;

public class main {
    public static void main(String[] args) {
            String fname = "./src/test1.bas";
            if (args.length > 0) {
                fname = args[0];
            }


            try(FileInputStream fs = new FileInputStream(fname)) {//読んだ後tryに入る
                LexicalUnit lexicalUnit;//空のをつくっている
                LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl(fs);//レキシカルアナライザーでファイルを開いている
                while(true) {
                    lexicalUnit = lexicalAnalyzer.get();//1文字読んだら呼ぶメソッドを選んでたやつ
                    System.out.println(lexicalUnit);//内容表示
                    if (lexicalUnit.getType() == LexicalType.EOF) {
                        break;
                    }
                }
            } catch (IOException e) {//例外処理
                System.out.println(fname + ": not found");
                System.exit(-1);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
