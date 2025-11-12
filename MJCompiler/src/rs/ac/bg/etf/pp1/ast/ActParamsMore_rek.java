// generated with ast extension for cup
// version 0.8
// 29/8/2025 11:57:21


package rs.ac.bg.etf.pp1.ast;

public class ActParamsMore_rek extends ActParamsMore {

    private ActParams ActParams;
    private ActParamsMore ActParamsMore;

    public ActParamsMore_rek (ActParams ActParams, ActParamsMore ActParamsMore) {
        this.ActParams=ActParams;
        if(ActParams!=null) ActParams.setParent(this);
        this.ActParamsMore=ActParamsMore;
        if(ActParamsMore!=null) ActParamsMore.setParent(this);
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActParams!=null) ActParams.accept(visitor);
        if(ActParamsMore!=null) ActParamsMore.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActParams!=null) ActParams.traverseTopDown(visitor);
        if(ActParamsMore!=null) ActParamsMore.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActParams!=null) ActParams.traverseBottomUp(visitor);
        if(ActParamsMore!=null) ActParamsMore.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParamsMore_rek(\n");

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
        buffer.append(") [ActParamsMore_rek]");
        return buffer.toString();
    }
}
