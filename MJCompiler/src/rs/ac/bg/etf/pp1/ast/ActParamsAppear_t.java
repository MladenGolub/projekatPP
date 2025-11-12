// generated with ast extension for cup
// version 0.8
// 29/8/2025 11:57:21


package rs.ac.bg.etf.pp1.ast;

public class ActParamsAppear_t extends ActParamsAppear {

    private ActParamsList ActParamsList;

    public ActParamsAppear_t (ActParamsList ActParamsList) {
        this.ActParamsList=ActParamsList;
        if(ActParamsList!=null) ActParamsList.setParent(this);
    }

    public ActParamsList getActParamsList() {
        return ActParamsList;
    }

    public void setActParamsList(ActParamsList ActParamsList) {
        this.ActParamsList=ActParamsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActParamsList!=null) ActParamsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActParamsList!=null) ActParamsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActParamsList!=null) ActParamsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParamsAppear_t(\n");

        if(ActParamsList!=null)
            buffer.append(ActParamsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParamsAppear_t]");
        return buffer.toString();
    }
}
