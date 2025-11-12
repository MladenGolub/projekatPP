// generated with ast extension for cup
// version 0.8
// 29/8/2025 11:57:21


package rs.ac.bg.etf.pp1.ast;

public class Expr_add extends Expr {

    private AddTermList AddTermList;

    public Expr_add (AddTermList AddTermList) {
        this.AddTermList=AddTermList;
        if(AddTermList!=null) AddTermList.setParent(this);
    }

    public AddTermList getAddTermList() {
        return AddTermList;
    }

    public void setAddTermList(AddTermList AddTermList) {
        this.AddTermList=AddTermList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AddTermList!=null) AddTermList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AddTermList!=null) AddTermList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AddTermList!=null) AddTermList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expr_add(\n");

        if(AddTermList!=null)
            buffer.append(AddTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Expr_add]");
        return buffer.toString();
    }
}
