// generated with ast extension for cup
// version 0.8
// 29/8/2025 11:57:21


package rs.ac.bg.etf.pp1.ast;

public class ActParamsAppear_e extends ActParamsAppear {

    private ActParamsListBegin ActParamsListBegin;

    public ActParamsAppear_e (ActParamsListBegin ActParamsListBegin) {
        this.ActParamsListBegin=ActParamsListBegin;
        if(ActParamsListBegin!=null) ActParamsListBegin.setParent(this);
    }

    public ActParamsListBegin getActParamsListBegin() {
        return ActParamsListBegin;
    }

    public void setActParamsListBegin(ActParamsListBegin ActParamsListBegin) {
        this.ActParamsListBegin=ActParamsListBegin;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActParamsListBegin!=null) ActParamsListBegin.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActParamsListBegin!=null) ActParamsListBegin.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActParamsListBegin!=null) ActParamsListBegin.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParamsAppear_e(\n");

        if(ActParamsListBegin!=null)
            buffer.append(ActParamsListBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParamsAppear_e]");
        return buffer.toString();
    }
}
