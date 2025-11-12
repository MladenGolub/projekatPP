package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.ActParams;
import rs.ac.bg.etf.pp1.ast.ActParamsAppear_e;
import rs.ac.bg.etf.pp1.ast.ActParamsAppear_t;
import rs.ac.bg.etf.pp1.ast.ActParamsList;
import rs.ac.bg.etf.pp1.ast.ActParamsListBegin;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.symboltable.concepts.Struct;

public class ActParamsCounter extends VisitorAdaptor {

	List<Struct> finalParamsList = new ArrayList<>();
	
	Stack<List<Struct>> actParamsLists = new Stack<>();
	
	@Override
	public void visit(ActParamsListBegin actParamslistBegin) {
		actParamsLists.push(new ArrayList<>());
	}
	
	@Override
	public void visit(ActParams actParams) {
		actParamsLists.peek().add(actParams.getExpr().struct);
	}
	
	@Override
	public void visit(ActParamsList actParamsList) {
		finalParamsList = actParamsLists.pop();
	}//svaki ActParamsList kada se zatvori skinuce svoju finalnu listu parametara
	
	@Override
	public void visit(ActParamsAppear_t actParams_appear_t) {
		
	}
	
	@Override
	public void visit(ActParamsAppear_e actParams_appear_e) {
	
	}
	
}

