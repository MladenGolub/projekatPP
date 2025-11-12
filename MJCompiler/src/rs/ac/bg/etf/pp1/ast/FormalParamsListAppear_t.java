// generated with ast extension for cup
// version 0.8
// 29/8/2025 11:57:21


package rs.ac.bg.etf.pp1.ast;

public class FormalParamsListAppear_t extends FormalParamsListAppear {

    private FormalParamsList FormalParamsList;

    public FormalParamsListAppear_t (FormalParamsList FormalParamsList) {
        this.FormalParamsList=FormalParamsList;
        if(FormalParamsList!=null) FormalParamsList.setParent(this);
    }

    public FormalParamsList getFormalParamsList() {
        return FormalParamsList;
    }

    public void setFormalParamsList(FormalParamsList FormalParamsList) {
        this.FormalParamsList=FormalParamsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormalParamsList!=null) FormalParamsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormalParamsList!=null) FormalParamsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormalParamsList!=null) FormalParamsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormalParamsListAppear_t(\n");

        if(FormalParamsList!=null)
            buffer.append(FormalParamsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormalParamsListAppear_t]");
        return buffer.toString();
    }
}
