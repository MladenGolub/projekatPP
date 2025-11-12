package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class GeneratorKoda extends VisitorAdaptor {

	private int mainPc;
	
	//Koristim stekove zato sto mogu ifovi da se ugnezdjuju i potreban mi je LIFo redosled
	//sami sebe ce skidati
	private Stack<Integer> condFalse = new Stack<>();
	private Stack<Integer> condTrue = new Stack<>();
	private Stack<Integer> skipElse = new Stack<>();
	private Stack<Integer> skipThen = new Stack<>();
	//stek koji koristimo za pocetak doWhile petlje
	private Stack<Integer> doWhileBegin = new Stack<>();
	//stekovi koje cemo koristiti za break i continue naredbe
	private Stack<Integer> forBegin = new Stack<>();
	//stek koji ce mi cuvati adrese na koje skacem za pocetak stmnt u foru
	private Stack<Integer> forEnd = new Stack<>();
	private Stack<Integer> forCond = new Stack<>();
	private Stack<List<Integer>> breakStack = new Stack<>();
	private Stack<List<Integer>> continueStack = new Stack<>();
	
	private Struct setType = Tab.find("set").getType();//dovukao sam set tip jer sam ga ja sam dodao
	
	private boolean newSet = false;
	
	public int getMainPc() {
		return mainPc;
	}
	
	private void addMeth() {//s je skup, a b je neka int vr. dodajemo je u skup ako ona ne postoji u skupu
		//pisemo kod koji treba da se generise kada ulazimo u add metodu, isto kao sto smo pisali i za ord i chr i len
		Obj addMeth = Tab.find("add");
		addMeth.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(2);//2 formalna parametra
		Code.put(4);//ukupni broj parametara, formalnih plus lokalnih (s, b, size, len, i)
		
		//int i = 1;
		Code.loadConst(1);
		Code.put(Code.store_2);//stavljamo i u lokalnu memoriju fje
		
		//int size = s[0];//koliko el ima u nizu
		Code.put(Code.load_n);
		Code.loadConst(0);
		Code.put(Code.aload);//ucitavam stvarnu velicinu niza, tj koliko ima elemenata
		Code.put(Code.store_3);//stavljamo size u lokalnu memoriju fje
		
		//for(; i < size; i++) { if(size[i] == b) return; }
		//stek je trenutno prazan
		int forProvera = Code.pc;//adresa koju popravljamo za skok, za sledecu proveru
		
		Code.put(Code.load_2);//ucitavam i
		Code.put(Code.load_3);//ucitavam size
		Code.put(Code.jcc + Code.ge);//provera da li je i < size + 1, ali je proveravamo suprotno jer ako jeste i >= size mi izlazimo iz petlje
		int forExit = Code.pc;
		Code.put2(0);//stavljamo 0 i cekamo skok
		
		//ako je i < size
		
		Code.put(Code.load_n);//adr
		Code.put(Code.load_2);//index (i)
		Code.put(Code.aload);//na stek stavljamo s[i]
		
		Code.put(Code.load_1);//b
		
		//stack: s[i] | b
		Code.put(Code.jcc + Code.eq);//ako su isti skacemo, jer ne traba u skup dodavati iste elemente
		int pronadjen = Code.pc;
		Code.put2(0);//cekamo skok, tj popravku skoka
		
		//ako nije nadjen
		Code.put(Code.load_2);//stack: i
		Code.loadConst(1);//stack: i | 1
		Code.put(Code.add);//stack: i++
		Code.put(Code.store_2);//upisujemo u i
		
		Code.putJump(forProvera);//skacemo opet na proveru uslova
		
		Code.fixup(forExit);
		
		Code.put(Code.load_3);//ucitavamo size
		Code.put(Code.load_n);//stack: size | adr
		Code.put(Code.arraylength);//stack: size | len
		Code.put(Code.jcc + Code.ge);// ako je size >= len, onda iskacemo jer nam je pun set
		int pun = Code.pc;
		Code.put2(0);//cekamo popravku za skok
		
		//ako set nije pun
		
		Code.put(Code.load_n);//adr
		Code.put(Code.load_3);//index (size)
		Code.put(Code.load_1);//value (b)
		Code.put(Code.astore);// adr[index] = value
		
		//moramo da uvelicamo size
		Code.put(Code.load_n);//adr
		Code.loadConst(0);//index
		Code.put(Code.load_3);
		Code.loadConst(1);
		Code.put(Code.add);// adr | index(0) | size + 1
		Code.put(Code.astore);//adr[index] = size + 1
		
		Code.fixup(pronadjen);
		Code.fixup(pun);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
}
	
	private void addAllMethod() {//u skup s se dodaju svi el niza b
		Obj addAllMeth = Tab.find("addAll");
		addAllMeth.setAdr(Code.pc);
		
		int addMetodaAdr = Tab.find("add").getAdr();//cuvamo adresu za add fje da bi mogli da je pozivamo za svaki el niza posebno
		
		Code.put(Code.enter);
		Code.put(2);//2 formalna parametra
		Code.put(4);//ukupni broj parametara, formalnih plus lokalnih (s, b[], len, i)
		
		//int i = 0;
		Code.loadConst(0);
		Code.put(Code.store_2);//u i upisujemo 0
		
		//int len = b.size();
		Code.put(Code.load_1);//ucitavamo niz
		Code.put(Code.arraylength);//racunamo duzinu niza
		Code.put(Code.store_3);
		
		//for (; i < len; i++) { add(s, b[i]); }
		int provera = Code.pc;
		Code.put(Code.load_2);//stack: i
		Code.put(Code.load_3);//stack: i | len
		Code.put(Code.jcc + Code.ge);// i >= len skacemo zavrsili smo
		int krajNiza = Code.pc;
		Code.put2(0);
		
		Code.put(Code.load_n);//stack: s
		Code.put(Code.load_1);//stack: s | b
		Code.put(Code.load_2);//stack: s | b | i
		Code.put(Code.aload);// stack: s | b[i]
		Code.put(Code.call);
		Code.put2(addMetodaAdr - Code.pc + 1);//pozivamo metodu
		
		Code.put(Code.load_2);//stack: i
		Code.loadConst(1);// stack: i | 1
		Code.put(Code.add);//stack: i++
		Code.put(Code.store_2);//i = i++;
		Code.putJump(provera);
		
		Code.fixup(krajNiza);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
	
		
	}
	
	private void initUniverseMeth() {
		//generisemo kod za metode ord, chr i len
		Obj ordMethod = Tab.find("ord");
		Obj chrMethod = Tab.find("chr");
		ordMethod.setAdr(Code.pc);
		chrMethod.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Obj lenMethod = Tab.find("len");
		lenMethod.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
	}
	
	GeneratorKoda() {
		
		this.initUniverseMeth();
		this.addMeth();
		this.addAllMethod();
		
	}
	
	//MethodDecl
	
	@Override
	public void visit(MethodTypeName_void methodTypeName_void) {
		methodTypeName_void.obj.setAdr(Code.pc);
		if(methodTypeName_void.obj.getName().equalsIgnoreCase("main")) {
			this.mainPc = Code.pc;
		}
		Code.put(Code.enter);
		Code.put(methodTypeName_void.obj.getLevel()); //b1 - broj fParametara
		Code.put(methodTypeName_void.obj.getLocalSymbols().size()); //b2 - broj lokalnih promenljivih
	}
	
	@Override
	public void visit(MethodTypeName_type methodTypeName_type) {
		methodTypeName_type.obj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(methodTypeName_type.obj.getLevel()); //b1 - broj fParametara
		Code.put(methodTypeName_type.obj.getLocalSymbols().size()); //b2 - broj lokalnih promenljivih
	}
	
	@Override
	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	//Statements
	
	@Override
	public void visit(Statement_printe statement_printe) {
		
		if(statement_printe.getExpr().struct.equals(Tab.charType)) {
			Code.loadConst(0);
			Code.put(Code.bprint);//ako nam je char u pitanju onda cemo ispisati samo donji bajt
		} else if(statement_printe.getExpr().struct.equals(setType)) {
			//ako je u pitanju set koji treba da se ispise moraju se ispisati svi clanovi odvojeni zarezom
			
			//stack: adr (seta)
			Code.put(Code.dup);//stack: adr | adr
			Code.loadConst(0);//stack: adr | adr | index
			Code.put(Code.aload);//stack: adr | adr[0]
			Code.loadConst(1);//stack: adr | adr[0] | 1
			Code.put(Code.jcc + Code.le);//ako je velicina niza manja od 1 izlazimo
			int prazan = Code.pc;
			Code.put2(0);
			
			//int i = 1;
			Code.loadConst(1);//stack: adr | 1
			Code.put(Code.store_3);//stack: adr
			
			int loop = Code.pc;
			Code.put(Code.dup);//stack: adr | adr
			Code.loadConst(0);//stack: adr | adr | 0
			Code.put(Code.aload);//stack: adr | adr[0]
			Code.put(Code.load_3);////stack: adr | adr[0] | i
			Code.put(Code.jcc + Code.le);//provera da li je i >= size, ako nije skacemo dalje
			int obidjen = Code.pc;
			Code.put2(0); 
			//stack: adr
			
			Code.put(Code.dup);//stack: adr | adr
			Code.put(Code.load_3);//stack: adr | adr | i
			Code.put(Code.aload);//stack: adr | adr[i]
			
			//dohvatio sam element
			
			Code.loadConst(0);//sirina ispisa
			Code.put(Code.print);//stack: adr | adr[i] | 0
			Code.loadConst(32);//stack: adr | eol
			Code.loadConst(0);//stack: adr | eol | 0
			Code.put(Code.bprint);//stack: adr
			
			//moram i da inkrementiram
			Code.put(Code.load_3);
			Code.loadConst(1);
			Code.put(Code.add);//stack: adr | i++
			Code.put(Code.store_3);//stack: adr
			Code.putJump(loop);
			
			Code.fixup(obidjen);
			Code.fixup(prazan);
			
			Code.put(Code.pop);//moram da skinem adr sa steka da bi mi ostao cist
			
//			Code.put(Code.dup);          
//	        Code.put(Code.dup); 
//	        Code.loadConst(0);
//	        Code.put(Code.aload);
//	       
//	        
//	        //  i = 1	       
//	        Code.loadConst(1);           
//
//
//	        int loopStart = Code.pc;
//	        
//	        Code.put(Code.dup_x1);  
//	        Code.put(Code.dup_x1);
//	        Code.put(Code.pop);
//	        Code.put(Code.dup_x1);
//	        
//	        Code.put(Code.jcc + Code.ge);// if (i >= s[0]) exit loop
//	        int loopExit = Code.pc;
//	        Code.put2(0); 
//
//
//	        Code.put(Code.dup_x2);       
//	        Code.put(Code.pop);
//	        Code.put(Code.dup_x1);
//	        Code.put(Code.aload);        
//
//	        Code.loadConst(0);
//	        Code.put(Code.print);        // Print element
//	        
//	        Code.loadConst(32);
//	        Code.loadConst(0);
//	        Code.put(Code.bprint);       // Print space
//
//	        Code.loadConst(1);
//	        Code.put(Code.add);          // i = i + 1
//
//	        Code.put(Code.dup_x2);
//	        Code.put(Code.pop);
//	        Code.put(Code.dup_x2);
//	        Code.put(Code.pop);
//	        Code.put(Code.dup_x2);
//	        Code.put(Code.dup_x2);
//	        Code.put(Code.pop);
//	        
//	        Code.put(Code.jmp);
//	        Code.put2(loopStart - Code.pc + 1);
//
//	        Code.fixup(loopExit);
//
//	        Code.put(Code.pop);          
//	        Code.put(Code.pop);          
//	        Code.put(Code.pop);    
//	        Code.put(Code.pop);
//	        
//			return;
			
		} else {
			Code.loadConst(0);
			Code.put(Code.print);
		}
	}
	
	@Override
	public void visit(Statement_printen statement_printen) {
		Code.loadConst(statement_printen.getN2());//dodajemo duzinu za ispis
		if(statement_printen.getExpr().struct.equals(Tab.charType)) {
			Code.put(Code.bprint);//ako nam je char u pitanju onda cemo ispisati samo donji bajt
		} else 
			Code.put(Code.print);
	}
	
	@Override
	public void visit(Statement_return statement_return) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	@Override
	public void visit(Statement_returnex statement_returnex) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	@Override
	public void visit(Statement_read statement_read) {
		if(statement_read.getDesignator().obj.getType().equals(Tab.charType)) {
			Code.put(Code.bread);//ako treba da se cita samo jedan bajt
		} else 
			Code.put(Code.read);//ako se cita nesto drugo
		Code.store(statement_read.getDesignator().obj);//storujemo ono sto smo procitali
	}
	
	@Override
	public void visit(ElseStatement_e elseStatement_e) {//ovde ulazimo ako nemamo else statement tako da moramo da pustimo
		//one koje su cekale else a njega nema i odavde isto zajedno nastavljaju svoj put
		//tacne
		Code.fixup(skipThen.pop());
		//tacne + netacne
	}
	
	@Override
	public void visit(ElseStatement_have elseStatement_have) {//ovo se obilazi na kraju else statement-a, zato moramo da oslobodimo one koje su trebale da preskoce else 
		//jer se else zavrsio i sada moraju i one da se nastave
		//netcane
		Code.fixup(skipElse.pop()); //varacamo tacne koji su preskocili ELSE
		//netacne + tacne
	}
	
	@Override
	public void visit(ElseNonTerm elseNonTerm) {
		//tacne
		Code.putJump(0); //tacne bacamo na kraj ELSE
		skipElse.push(Code.pc - 2);
		Code.fixup(skipThen.pop());
		//netacne
	}
	
	//Do While petlja
	@Override
	public void visit(DoNonterminal doNonterminal) {
		doWhileBegin.push(Code.pc);
		breakStack.push(new ArrayList<Integer>());
		continueStack.push(new ArrayList<Integer>());
		//moramo da napravimo prve stekove za svaki slucaj ako budemo imali break ili continue naredbe
		//ovo se radi svaki put kada naidej nova do while petlja
	}
	
	@Override
	public void visit(Statemetn_do statement_do) {
		Code.putJump(doWhileBegin.pop());//one koje treba ponovo da se izvrse jer je uslov tacan i dalje stavljam da skoce nazad na pocetak Statementa celog
		Code.fixup(skipThen.pop());//one koje su trebale da preskoce then granu njih namestam da skoce ovde nakon while petlje
		
		while(!breakStack.peek().isEmpty()) {
			Code.fixup(breakStack.peek().remove(0));
		}
		breakStack.pop();//ispraznili smo stek koji je bio na vrhu i sada ga jos skidamo da nam ne bi pravio problem
	}
	
	@Override
	public void visit(WhileNonterminal whileNonterminal) {
		while(!continueStack.peek().isEmpty()) {
			Code.fixup(continueStack.peek().remove(0));//popravljamo jednu po jednu adresu
		}
		continueStack.pop();//skidamo listu sa vrha steka jer je sada ona prazna i pravice nam problem
	}
	
	//Break
	@Override
	public void visit(Statement_break statement_break) {
		Code.putJump(0);
		breakStack.peek().add(Code.pc - 2);//na stek na vrhu breakStack ubacujemo adresu koja treba da se skine posle i popravi
	}
	
	//Continue
	@Override
	public void visit(Statement_continue statement_continue) {
		Code.putJump(0);
		continueStack.peek().add(Code.pc - 2);//dodajemo adrese na kojima treba da popravimo skok
	}
	
	//For
//	@Override
//	public void visit(Statement_for Statement_for) {
//		if(!skipThen.empty()) {
//			Code.fixup(skipThen.pop());
//		}
//		while(!breakStack.peek().isEmpty()) {
//			Code.fixup(breakStack.peek().remove(0));//pustamo sve koji su odradili break
//		}
//		breakStack.pop();
//	}
//	
//	@Override
//	public void visit(ForNonTerminal forNonTerminal) {
//		
////		breakStack.push(new ArrayList<Integer>());
////		continueStack.push(new ArrayList<Integer>());		
//	}
//	
//	@Override
//	public void visit(SemiNonTerminal semiNonTerminal) {
//		Code.putJump(0);
//		forBegin.push(Code.pc - 2);//blokiram nit, dok ne sazna gde treba da skoci
//		
//		forEnd.push(Code.pc);//pustam one koje su stigle na kraj fora
//	}//ovo je kraj CondList u for petlji
//	
//	@Override
//	public void visit(ForBeginNonTerminal forBeginNonTerminal) {
//		Code.fixup(forBegin.pop());//popravljam da skace ovde
//		breakStack.push(new ArrayList<Integer>());
//		continueStack.push(new ArrayList<Integer>());
//		//pravim stekove za break i continue
//	}//ovo mi je pocetak stmnt u for petlji
//	
//	@Override
//	public void visit(ForEndNonTerminal forEndNonTerminal) {
//		while(!continueStack.peek().isEmpty()) {
//			Code.fixup(continueStack.peek().remove(0));
//		}
//		continueStack.pop();
//		//moramo ovde da pustimo one koji su radili continue da bi odradili DesignatorStatement
//		Code.putJump(forEnd.pop());
//	}
//	
//	@Override
//	public void visit(RparenNonTerminal rparenNonTerminal) {
//		Code.putJump(forCond.pop());//skacem na uslov ponovo
//	}
//	
//	@Override
//	public void visit(CondFor condFor) {
//		forCond.push(Code.pc);
//	}
	
	//Expr
	
	@Override
	public void visit(Expr_map expr_map) {
		int funkcija = expr_map.getDesignator().obj.getAdr();//uzimamo adresu fje
		
		Obj niz = expr_map.getDesignator1().obj;
		
		Code.loadConst(0);//suma
		Code.loadConst(0);//i
	
		int loop = Code.pc;
		Code.put(Code.dup);
		Code.load(niz);//stack: suma | i | i | niz
		Code.put(Code.arraylength);//stack: suma | i | i | len
		Code.put(Code.jcc + Code.ge);
		int krajNiza = Code.pc;
		Code.put2(0);//cekamo popravku skoka
		//stack: suma | i
		
		Code.put(Code.dup);
		Code.load(niz);//stack: suma | i | i | niz
		Code.put(Code.dup_x1);//stack: suma | i | niz | i | niz
		Code.put(Code.pop);//stack: suma | i | niz | i
		Code.put(Code.aload);//stack: suma | i | niz[i]
		Code.put(Code.call);
		Code.put2(funkcija - Code.pc + 1);//zovemo fju koju smo dobili u Designatoru
		//stack: suma | i | val - poratna vr iz fje
		Code.put(Code.dup_x2);//stack: val | suma | i | val
		Code.put(Code.pop);//stack: val | suma | i
		Code.put(Code.dup_x2);//stack: i | val | sum | i
		Code.put(Code.pop);//stack: i | val | sum 
		Code.put(Code.add);//stack: i | sum+val
		Code.put(Code.dup_x1);//stack: sum+val | i | sum+val
		Code.put(Code.pop);//sum+val | i
		Code.loadConst(1);//stack: sum+val | i | 1
		Code.put(Code.add);//stack: sum+val | i+1
		Code.putJump(loop);
		
		Code.fixup(krajNiza);
		Code.put(Code.pop);
		//Treba samo na steku ostaviti zbir svih poziva funkcije
		
	}
	
	@Override
	public void visit(AddTermList_add addTermList_add) {
		if(addTermList_add.getAddop() instanceof Addop_plus) {
			Code.put(Code.add);
		} else if(addTermList_add.getAddop() instanceof Addop_minus) {
			Code.put(Code.sub);
		}
	}
	
	@Override
	public void visit(MulFactorList_mul mulFactorList_mul) {
		if(mulFactorList_mul.getMulop() instanceof Mulop_mul) {
			Code.put(Code.mul);
		} else if(mulFactorList_mul.getMulop() instanceof Mulop_div) {
			Code.put(Code.div);
		} else if(mulFactorList_mul.getMulop() instanceof Mulop_mod) {
			Code.put(Code.rem);
		}
	}
	
	//FactorSub
	
	@Override
	public void visit(FactorSub_n factorSub_n) {
		Code.loadConst(factorSub_n.getN1());
	}
	
	@Override
	public void visit(FactorSub_c factorSub_c) {
		Code.loadConst(factorSub_c.getC1());
	}
	
	@Override
	public void visit(FactorSub_b factorSub_b) {
		Code.loadConst(factorSub_b.getB1());
	}
	
	@Override
	public void visit(FactorSub_d factorSub_d) {
		Code.load(factorSub_d.getDesignator().obj);
	}
	
	@Override
	public void visit(FactorSub_arr factorSub_arr) {
		//ako mi dodje da pravim skup(set) ja zelim da za njega rezervisem n + 1 mesto jer cu u tom nultom mestu cuvati velicinu skupa
		
		if(factorSub_arr.getType().struct.equals(setType)) {
			Code.loadConst(1);
			Code.put(Code.add);//na steku ce mi se vec nalaziti Expr, koji predstavlja velicinu, a ja cu ga povecati za 1
		}//ovo se dodaje samo ako je set u pitanju jer ako je obican niz u pitanju samo ce se alocirati koliko je potrebno
		
		Code.put(Code.newarray);//kada stavim newarray instrulciju ona ceka jos jedan podatak pre nego sto
		//se izvrsi a taj podatak mi govori da li ce mi to biti niz bajtova ili reci
		
		if(factorSub_arr.getType().struct.equals(Tab.charType)) {
			Code.put(0);
		} else if(factorSub_arr.getType().struct.equals(Tab.intType)) {
			Code.put(1);
		} else if(factorSub_arr.getType().struct.equals(setType)) {//ako nije char, a nije ni int mora biti set jer je jedino moguce da bude set
			newSet = true;//posto nemam konstantu za set koju stavljam na stek moram sebi da kazem da se set napravio
			Code.put(1);
		}
	}
	
	@Override
	public void visit(FactorSub_function factorSub_function) {
		int pomeraj = factorSub_function.getDesignator().obj.getAdr() - Code.pc;
		
		Code.put(Code.call);
		Code.put2(pomeraj);
	}
	
	//Designator
	
	@Override
	public void visit(DesignatorArrName designatorArrName) {
		Code.load(designatorArrName.obj);//kacimo adresu niza kada naidjemo na njega
	}
	
	//DesignatorStatement
	
	@Override
	public void visit(DesignatorStatement_ass designatorStatement_ass) {
		Code.store(designatorStatement_ass.getDesignator().obj);//mi designator samo storujemo
		//ali ako je designator ime za set
		if(newSet == true) {
			newSet = false;
			Code.load(designatorStatement_ass.getDesignator().obj);//stavljam adresu seta na stek
			Code.loadConst(0);//index 0
			Code.loadConst(1);//stavljam vr. 1 da mi kaze da je u pitanju set a ne niz
			Code.put(Code.astore);//cuvam na set[0] = 1, kada pristupim toj promenljivoj kasnije znacu da li je set ili ne
		}
	}
	
	@Override
	public void visit(DesignatorStatement_actpar designatorStatement_actpar) {
		int pomeraj = designatorStatement_actpar.getDesignator().obj.getAdr() - Code.pc;
		
		Code.put(Code.call);
		Code.put2(pomeraj);
		
		if(designatorStatement_actpar.getDesignator().obj.getType() != Tab.noType) {
			Code.put(Code.pop);//moramo da skinemo ako nam je tip razlicit od void jer ce nam povratna vr ostati na steku i pravice djubre
		}
	}
	
	@Override
	public void visit(DesignatorStatement_inc designatorStatement_inc) {
		if(designatorStatement_inc.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}//ako je u pitanju inc ili dec od elem niza moramo da dupliramo adresu i index da bi nam ostali za storovanje
		Code.load(designatorStatement_inc.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(designatorStatement_inc.getDesignator().obj);
	}
	
	@Override
	public void visit(DesignatorStatement_dec designatorStatement_dec) {
		if(designatorStatement_dec.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(designatorStatement_dec.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(designatorStatement_dec.getDesignator().obj);
	}
	
	@Override
	public void visit(DesignatorStatement_setop designatorStatement_setop) {
		
		Obj desObj = designatorStatement_setop.getDesignator().obj;
		Obj desObj1 = designatorStatement_setop.getDesignator1().obj;
		Obj desObj2 = designatorStatement_setop.getDesignator2().obj;
		Obj add = Tab.find("add");
		
		// if set1 full exitFull
		Code.load(desObj);
		Code.loadConst(0);
		Code.put(Code.aload);
		Code.load(desObj);
		Code.put(Code.arraylength);
		Code.put(Code.jcc + Code.ge);
		int exitFull = Code.pc;
		Code.put2(0);
		
		// i = 1
		Code.loadConst(1);
		
		int first = Code.pc;
		Code.put(Code.dup);
		
		Code.load(desObj1);
		Code.loadConst(0);
		Code.put(Code.aload);
		
		// if (i >= set2[0]) second
		Code.put(Code.jcc + Code.ge);
		int firstExit = Code.pc;
		Code.put2(0);
		
		Code.put(Code.dup);
		
		// set2[i]
		Code.load(desObj1);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.aload);
		
		// call add(set1, set2[i])
		Code.load(desObj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.call);	
		Code.put2(add.getAdr() - Code.pc + 1);
		
		// i++
		Code.loadConst(1);
		Code.put(Code.add);
		
		Code.putJump(first);
		
		Code.fixup(firstExit);
		
		// i = 1
		Code.loadConst(1);
		
		int second = Code.pc;
		Code.put(Code.dup);
	
		Code.load(desObj2);
		Code.loadConst(0);
		Code.put(Code.aload);
		
		// if (i >= set3[0]) end
		Code.put(Code.jcc + Code.ge);
		int secondExit = Code.pc;
		Code.put2(0);
		
		Code.put(Code.dup);
		
		// set3[i]
		Code.load(desObj2);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.aload);
		
		// call add(set1, set3[i])
		Code.load(desObj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.call);
		Code.put2(add.getAdr() - Code.pc + 1);
		
		// i++
		Code.loadConst(1);
		Code.put(Code.add);
		
		Code.putJump(second);
		
		Code.fixup(secondExit);
		
		Code.put(Code.pop);
		Code.put(Code.pop);
		
		Code.fixup(exitFull);
		
	}
	
	
	//Factor
	@Override
	public void visit(Factor_m factor_m) {
		Code.put(Code.neg);//samo cemo negirati poslednju stvar stavljenu na stek jer znamo da se pre ovog visita odradio FactorSub
	}
	
	//Condition
	private int getRelop(Relop relop) {
		if(relop instanceof Relop_eq) {
			return Code.eq;
		} else if(relop instanceof Relop_neq) {
			return Code.ne;
		} else if(relop instanceof Relop_grt) {
			return Code.gt;
		} else if(relop instanceof Relop_gre) {
			return Code.ge;
		} else if(relop instanceof Relop_less) {
			return Code.lt;
		} else if(relop instanceof Relop_lesse) {
			return Code.le;
		} else {
			return -1;
		}
	}
	
	@Override
	public void visit(CondFact_single condFact_single) {
		Code.loadConst(0);
		Code.putFalseJump(Code.ne, 0); //netacna
		condFalse.push(Code.pc - 2);
		//tacna
	}
	
	@Override
	public void visit(CondFact_relopExpr condFact_relopExpr) {
		Code.putFalseJump(getRelop(condFact_relopExpr.getRelop()), 0); //netacna
		condFalse.push(Code.pc - 2);
		//tacna
	}
	
	@Override
	public void visit(CondTerm condTerm) {
		//tacne
		Code.putJump(0);//tacne bacamo na THEN
		condTrue.push(Code.pc - 2);
		//ovde vracam netacne
		while(!condFalse.empty())
			Code.fixup(condFalse.pop());
		//netacne
	}
	
	@Override
	public void visit(Condition_list condition_list) {
		//netcni
		Code.putJump(0); //netacne bacamo na ELSE
		skipThen.push(Code.pc - 2);
		//THEN
		while(!condTrue.empty())
			Code.fixup(condTrue.pop());
		//tacne
	}
	
}
