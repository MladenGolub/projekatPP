// generated with ast extension for cup
// version 0.8
// 29/8/2025 11:57:21


package rs.ac.bg.etf.pp1.ast;

public class ActParamsList implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ActParamsListBegin ActParamsListBegin;
    private ActParams ActParams;
    private ActParamsMore ActParamsMore;

    public ActParamsList (ActParamsListBegin ActParamsListBegin, ActParams ActParams, ActParamsMore ActParamsMore) {
        this.ActParamsListBegin=ActParamsListBegin;
        if(ActParamsListBegin!=null) ActParamsListBegin.setParent(this);
        this.ActParams=ActParams;
        if(ActParams!=null) ActParams.setParent(this);
        this.ActParamsMore=ActParamsMore;
        if(ActParamsMore!=null) ActParamsMore.setParent(this);
    }

    public ActParamsListBegin getActParamsListBegin() {
        return ActParamsListBegin;
    }

    public void setActParamsListBegin(ActParamsListBegin ActParamsListBegin) {
        this.ActParamsListBegin=ActParamsListBegin;
    }

    public ActParams getActParams() {
        return ActParams;
    }

    public void setActParams(ActParams ActParams) {
        this.ActParams=ActParams;
    }

    public ActParamsMore getActParamsMore() {
        return ActParamsMore;
    }

    public void setActParamsMore(ActParamsMore ActParamsMore) {
        this.ActParamsMore=ActParamsMore;
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
        if(ActParamsListBegin!=null) ActParamsListBegin.accept(visitor);
        if(ActParams!=null) ActParams.accept(visitor);
        if(ActParamsMore!=null) ActParamsMore.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActParamsListBegin!=null) ActParamsListBegin.traverseTopDown(visitor);
        if(ActParams!=null) ActParams.traverseTopDown(visitor);
        if(ActParamsMore!=null) ActParamsMore.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActParamsListBegin!=null) ActParamsListBegin.traverseBottomUp(visitor);
        if(ActParams!=null) ActParams.traverseBottomUp(visitor);
        if(ActParamsMore!=null) ActParamsMore.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParamsList(\n");

        if(ActParamsListBegin!=null)
            buffer.append(ActParamsListBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParams!=null)
            buffer.append(ActParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParamsMore!=null)
            buffer.append(ActParamsMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParamsList]");
        return buffer.toString();
    }
}
