// generated with ast extension for cup
// version 0.8
// 29/8/2025 11:57:21


package rs.ac.bg.etf.pp1.ast;

public class ConVarDeclList_con extends ConVarDeclList {

    private ConVarDeclList ConVarDeclList;
    private ConDeclList ConDeclList;

    public ConVarDeclList_con (ConVarDeclList ConVarDeclList, ConDeclList ConDeclList) {
        this.ConVarDeclList=ConVarDeclList;
        if(ConVarDeclList!=null) ConVarDeclList.setParent(this);
        this.ConDeclList=ConDeclList;
        if(ConDeclList!=null) ConDeclList.setParent(this);
    }

    public ConVarDeclList getConVarDeclList() {
        return ConVarDeclList;
    }

    public void setConVarDeclList(ConVarDeclList ConVarDeclList) {
        this.ConVarDeclList=ConVarDeclList;
    }

    public ConDeclList getConDeclList() {
        return ConDeclList;
    }

    public void setConDeclList(ConDeclList ConDeclList) {
        this.ConDeclList=ConDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConVarDeclList!=null) ConVarDeclList.accept(visitor);
        if(ConDeclList!=null) ConDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConVarDeclList!=null) ConVarDeclList.traverseTopDown(visitor);
        if(ConDeclList!=null) ConDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConVarDeclList!=null) ConVarDeclList.traverseBottomUp(visitor);
        if(ConDeclList!=null) ConDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConVarDeclList_con(\n");

        if(ConVarDeclList!=null)
            buffer.append(ConVarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConDeclList!=null)
            buffer.append(ConDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConVarDeclList_con]");
        return buffer.toString();
    }
}
