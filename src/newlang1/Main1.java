package newlang1;

import java.io.*;

public class Main1 {
    public static void main(String[] args) {
        String fname = "./src/test1.bas";
        if (args.length > 0) {
            fname = args[0];
        }

        Reader fr=null;
        try {
            fr = new FileReader(fname);
        } catch (FileNotFoundException e) {
            System.out.println(fname + ": not found");
            System.exit(-1);
        }

        while (true) {
            int ci;
            try {
                ci = fr.read();
            } catch (IOException e) {
                System.out.println("io error");
                break;
            }
            if (ci == -1) break;
            System.out.print((char)ci);
        }
        if (fr != null){
            try {
                fr.close();
            } catch (IOException e) {
            }
        }

//        TODO Auto-generated method stub
//        try {
//            // 1.ファイルのパスを指定する
//            File file = new File("./src/test1.bas");
//
//            // 2.ファイルが存在しない場合に例外が発生するので確認する
//            if (!file.exists()) {
//                System.out.print("ファイルが存在しません");
//                return;
//            }
//
//            // 3.FileReaderクラスとreadメソッドを使って1文字ずつ読み込み表示する
//            FileReader fileReader = new FileReader(file);
//            int data;
//            while ((data = fileReader.read()) != -1) {
//                System.out.print((char) data);
//            }
//
//            // 4.最後にファイルを閉じてリソースを開放する
//            fileReader.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }
}
