package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class Compiler {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(Compiler.class);
		
		Reader br = null;
		try {
			File sourceCode = new File("test/test.mj");
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			/* formiranje AST */
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  
	        
	        Program prog = (Program)(s.value); 
	        
			// ispis AST
			log.info(prog.toString(""));
			log.info("=====================================================================");
			
			/* Inicijalizacija tabele simbola */

			Tab.init();
			Struct boolType = new Struct(Struct.Bool);
			Obj boolObj = Tab.insert(Obj.Type, "bool", boolType);
			boolObj.setAdr(-1);
	        boolObj.setLevel(-1);
	        
	        Struct setType = new Struct(Struct.Array, Tab.intType);
	        Obj setObj = Tab.insert(Obj.Type, "set", setType);
	        setObj.setAdr(-1);
	        setObj.setLevel(-1);
	        
	        Obj addMethObj = Tab.insert(Obj.Meth, "add", Tab.noType);
	        addMethObj.setAdr(0);
	        addMethObj.setLevel(2);
			{
				Tab.openScope();
				Tab.currentScope.addToLocals(new Obj(Obj.Var, "a", setType, 0, 1));
				Tab.currentScope.addToLocals(new Obj(Obj.Var, "b", Tab.intType, 0, 1));
				addMethObj.setLocals(Tab.currentScope.getLocals());
				Tab.closeScope();
				for(Obj fp: Tab.find("add").getLocalSymbols())
	        		fp.setFpPos(1);
			}//dodajem metode u tabelu simbola zajedno sa njihovim formalnim parametrima 
			
			Obj addAllMethObj = Tab.insert(Obj.Meth, "addAll", Tab.noType);
	        addAllMethObj.setAdr(0);
	        addAllMethObj.setLevel(2);
			{
				Tab.openScope();
				Tab.currentScope.addToLocals(new Obj(Obj.Var, "a", setType, 0, 1));
				Tab.currentScope.addToLocals(new Obj(Obj.Var, "b", new Struct(Struct.Array, Tab.intType), 0, 1));
				addAllMethObj.setLocals(Tab.currentScope.getLocals());
				Tab.closeScope();
				for(Obj fp: Tab.find("addAll").getLocalSymbols())
	        		fp.setFpPos(1);
			}//dodajem metode u tabelu simbola zajedno sa njihovim formalnim parametrima
			
			/* Semanticka anazila */
			SemantickiAnalizator sa = new SemantickiAnalizator();
			prog.traverseBottomUp(sa);
			
			/* Ispis tabele simbola */
			log.info("=====================================================================");
			Tab.dump();
			
			
			if(!p.errorDetected && sa.passed()){
				/* Generisanje koda */
				
				File objFile = new File("test/test.obj");
				if(objFile.exists()) objFile.delete();
				
				GeneratorKoda genKoda = new GeneratorKoda();
				prog.traverseBottomUp(genKoda);
				Code.dataSize = sa.nVars;
				Code.mainPc = genKoda.getMainPc();
				Code.write(new FileOutputStream(objFile));
						
				log.info("Generisanje uspesno zavrseno!");
			}else{
				log.error("Parsiranje NIJE uspesno zavrseno!");
			}
			
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}
	
	
}
