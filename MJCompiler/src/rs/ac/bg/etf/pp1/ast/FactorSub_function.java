// generated with ast extension for cup
// version 0.8
// 29/8/2025 11:57:21


package rs.ac.bg.etf.pp1.ast;

public class FactorSub_function extends FactorSub {

    private Designator Designator;
    private ActParamsAppear ActParamsAppear;

    public FactorSub_function (Designator Designator, ActParamsAppear ActParamsAppear) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.ActParamsAppear=ActParamsAppear;
        if(ActParamsAppear!=null) ActParamsAppear.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public ActParamsAppear getActParamsAppear() {
        return ActParamsAppear;
    }

    public void setActParamsAppear(ActParamsAppear ActParamsAppear) {
        this.ActParamsAppear=ActParamsAppear;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(ActParamsAppear!=null) ActParamsAppear.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(ActParamsAppear!=null) ActParamsAppear.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(ActParamsAppear!=null) ActParamsAppear.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorSub_function(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParamsAppear!=null)
            buffer.append(ActParamsAppear.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorSub_function]");
        return buffer.toString();
    }
}
