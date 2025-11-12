// generated with ast extension for cup
// version 0.8
// 29/8/2025 11:57:21


package rs.ac.bg.etf.pp1.ast;

public class MethodDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private MethodTypeName MethodTypeName;
    private FormalParamsListAppear FormalParamsListAppear;
    private VarDeclListNbr VarDeclListNbr;
    private StatementList StatementList;

    public MethodDecl (MethodTypeName MethodTypeName, FormalParamsListAppear FormalParamsListAppear, VarDeclListNbr VarDeclListNbr, StatementList StatementList) {
        this.MethodTypeName=MethodTypeName;
        if(MethodTypeName!=null) MethodTypeName.setParent(this);
        this.FormalParamsListAppear=FormalParamsListAppear;
        if(FormalParamsListAppear!=null) FormalParamsListAppear.setParent(this);
        this.VarDeclListNbr=VarDeclListNbr;
        if(VarDeclListNbr!=null) VarDeclListNbr.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public MethodTypeName getMethodTypeName() {
        return MethodTypeName;
    }

    public void setMethodTypeName(MethodTypeName MethodTypeName) {
        this.MethodTypeName=MethodTypeName;
    }

    public FormalParamsListAppear getFormalParamsListAppear() {
        return FormalParamsListAppear;
    }

    public void setFormalParamsListAppear(FormalParamsListAppear FormalParamsListAppear) {
        this.FormalParamsListAppear=FormalParamsListAppear;
    }

    public VarDeclListNbr getVarDeclListNbr() {
        return VarDeclListNbr;
    }

    public void setVarDeclListNbr(VarDeclListNbr VarDeclListNbr) {
        this.VarDeclListNbr=VarDeclListNbr;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodTypeName!=null) MethodTypeName.accept(visitor);
        if(FormalParamsListAppear!=null) FormalParamsListAppear.accept(visitor);
        if(VarDeclListNbr!=null) VarDeclListNbr.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodTypeName!=null) MethodTypeName.traverseTopDown(visitor);
        if(FormalParamsListAppear!=null) FormalParamsListAppear.traverseTopDown(visitor);
        if(VarDeclListNbr!=null) VarDeclListNbr.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodTypeName!=null) MethodTypeName.traverseBottomUp(visitor);
        if(FormalParamsListAppear!=null) FormalParamsListAppear.traverseBottomUp(visitor);
        if(VarDeclListNbr!=null) VarDeclListNbr.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDecl(\n");

        if(MethodTypeName!=null)
            buffer.append(MethodTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormalParamsListAppear!=null)
            buffer.append(FormalParamsListAppear.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclListNbr!=null)
            buffer.append(VarDeclListNbr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDecl]");
        return buffer.toString();
    }
}
