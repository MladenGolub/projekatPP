// generated with ast extension for cup
// version 0.8
// 29/8/2025 11:57:21


package rs.ac.bg.etf.pp1.ast;

public class VarDeclListNbr_rek extends VarDeclListNbr {

    private VarDeclListNbr VarDeclListNbr;
    private VarDeclList VarDeclList;

    public VarDeclListNbr_rek (VarDeclListNbr VarDeclListNbr, VarDeclList VarDeclList) {
        this.VarDeclListNbr=VarDeclListNbr;
        if(VarDeclListNbr!=null) VarDeclListNbr.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
    }

    public VarDeclListNbr getVarDeclListNbr() {
        return VarDeclListNbr;
    }

    public void setVarDeclListNbr(VarDeclListNbr VarDeclListNbr) {
        this.VarDeclListNbr=VarDeclListNbr;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclListNbr!=null) VarDeclListNbr.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclListNbr!=null) VarDeclListNbr.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclListNbr!=null) VarDeclListNbr.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclListNbr_rek(\n");

        if(VarDeclListNbr!=null)
            buffer.append(VarDeclListNbr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListNbr_rek]");
        return buffer.toString();
    }
}
