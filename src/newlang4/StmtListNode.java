package newlang4;

public class StmtListNode extends Node{
    List<Node> child = new ArrayList<Node>();
    static Set<LexicalType> first = new HashSet<LexicalType>(
            ArrayUtil.asList(LexicalType.IF, LexicalType.DO,
                    LexicalType.NAME, LexicalType.FOR,
                    LexicalType.END)
    );

    static boolean isMatch(LexicalType t){
        return first.contains(t);
    }

    static Node getHandler(LexicalType t, Environment e){
        if(!first.contains(t)) return NULL;
        return new StmtListNode(e);
    }

    private StmtListNode(Environment e){
        super(e);
        type = NodeType.STMT_LIST;
    }

    public boolean Parse(){
        //ここになにか追記
    }
}