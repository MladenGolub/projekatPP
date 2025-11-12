// generated with ast extension for cup
// version 0.8
// 29/8/2025 11:57:21


package rs.ac.bg.etf.pp1.ast;

public class Statemetn_do extends Statement {

    private DoNonterminal DoNonterminal;
    private Statement Statement;
    private WhileNonterminal WhileNonterminal;
    private ConditionList ConditionList;

    public Statemetn_do (DoNonterminal DoNonterminal, Statement Statement, WhileNonterminal WhileNonterminal, ConditionList ConditionList) {
        this.DoNonterminal=DoNonterminal;
        if(DoNonterminal!=null) DoNonterminal.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.WhileNonterminal=WhileNonterminal;
        if(WhileNonterminal!=null) WhileNonterminal.setParent(this);
        this.ConditionList=ConditionList;
        if(ConditionList!=null) ConditionList.setParent(this);
    }

    public DoNonterminal getDoNonterminal() {
        return DoNonterminal;
    }

    public void setDoNonterminal(DoNonterminal DoNonterminal) {
        this.DoNonterminal=DoNonterminal;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public WhileNonterminal getWhileNonterminal() {
        return WhileNonterminal;
    }

    public void setWhileNonterminal(WhileNonterminal WhileNonterminal) {
        this.WhileNonterminal=WhileNonterminal;
    }

    public ConditionList getConditionList() {
        return ConditionList;
    }

    public void setConditionList(ConditionList ConditionList) {
        this.ConditionList=ConditionList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoNonterminal!=null) DoNonterminal.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(WhileNonterminal!=null) WhileNonterminal.accept(visitor);
        if(ConditionList!=null) ConditionList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoNonterminal!=null) DoNonterminal.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(WhileNonterminal!=null) WhileNonterminal.traverseTopDown(visitor);
        if(ConditionList!=null) ConditionList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoNonterminal!=null) DoNonterminal.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(WhileNonterminal!=null) WhileNonterminal.traverseBottomUp(visitor);
        if(ConditionList!=null) ConditionList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Statemetn_do(\n");

        if(DoNonterminal!=null)
            buffer.append(DoNonterminal.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(WhileNonterminal!=null)
            buffer.append(WhileNonterminal.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionList!=null)
            buffer.append(ConditionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Statemetn_do]");
        return buffer.toString();
    }
}
