// generated with ast extension for cup
// version 0.8
// 29/8/2025 11:57:21


package rs.ac.bg.etf.pp1.ast;

public class FormalParamsList implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private FormalParams FormalParams;
    private FormalParamsMore FormalParamsMore;

    public FormalParamsList (FormalParams FormalParams, FormalParamsMore FormalParamsMore) {
        this.FormalParams=FormalParams;
        if(FormalParams!=null) FormalParams.setParent(this);
        this.FormalParamsMore=FormalParamsMore;
        if(FormalParamsMore!=null) FormalParamsMore.setParent(this);
    }

    public FormalParams getFormalParams() {
        return FormalParams;
    }

    public void setFormalParams(FormalParams FormalParams) {
        this.FormalParams=FormalParams;
    }

    public FormalParamsMore getFormalParamsMore() {
        return FormalParamsMore;
    }

    public void setFormalParamsMore(FormalParamsMore FormalParamsMore) {
        this.FormalParamsMore=FormalParamsMore;
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
        if(FormalParams!=null) FormalParams.accept(visitor);
        if(FormalParamsMore!=null) FormalParamsMore.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormalParams!=null) FormalParams.traverseTopDown(visitor);
        if(FormalParamsMore!=null) FormalParamsMore.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormalParams!=null) FormalParams.traverseBottomUp(visitor);
        if(FormalParamsMore!=null) FormalParamsMore.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormalParamsList(\n");

        if(FormalParams!=null)
            buffer.append(FormalParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormalParamsMore!=null)
            buffer.append(FormalParamsMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormalParamsList]");
        return buffer.toString();
    }
}
