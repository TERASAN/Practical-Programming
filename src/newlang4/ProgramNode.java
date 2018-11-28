package newlang4;

import newlang3.Node;
import newlang3.NodeType;

public class ProgramNode extends Node {
    public boolean Parse() throws Exception{
        return true;
    }
    public String toString(){
        if(type == NodeType.END) return "END";
        else return "Node";
    }

}
