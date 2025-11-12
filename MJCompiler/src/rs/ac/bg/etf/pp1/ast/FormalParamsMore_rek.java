// generated with ast extension for cup
// version 0.8
// 29/8/2025 11:57:21


package rs.ac.bg.etf.pp1.ast;

public class FormalParamsMore_rek extends FormalParamsMore {

    private FormalParams FormalParams;
    private FormalParamsMore FormalParamsMore;

    public FormalParamsMore_rek (FormalParams FormalParams, FormalParamsMore FormalParamsMore) {
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
        buffer.append("FormalParamsMore_rek(\n");

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
        buffer.append(") [FormalParamsMore_rek]");
        return buffer.toString();
    }
}
