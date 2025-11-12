package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;


public class SemantickiAnalizator extends VisitorAdaptor {
	
	
	
	private boolean errorDetected = false;
	
	Logger log = Logger.getLogger(getClass());

	private Obj currentProg;
	private Struct currentType;
	private int constant;
	private Struct conType;
	private Struct boolType = Tab.find("bool").getType();
	private Struct setType = Tab.find("set").getType();
	private Obj mainMeth;
	private Obj currentMeth;
	private boolean returnHappend = false;
	private int cntDo = 0;

	int nVars;
	
	//Metode za ispis
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if(line != 0) {
			msg.append(" na liniji ").append(line);
		}
		log.error(msg.toString());
	}
	
	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}

	public boolean passed() {
		return !errorDetected;
	}
	
	//SEMANTICKI OBILAZAK CVOROVA
	
	@Override
	public void visit(ProgName progName) {
		//cuvam u mojoj klasi pokazivac na jedan Obj cvor koji predstavlja cvor mog programa
		nVars = Tab.currentScope().getnVars();
		currentProg = Tab.insert(Obj.Prog, progName.getI1(), Tab.noType);
		Tab.openScope();
	}
	
	@Override
	public void visit(Program program) {
		nVars = Tab.currentScope().getnVars();
		Tab.chainLocalSymbols(currentProg);
		Tab.closeScope();
		currentProg = null;
		
		if(mainMeth == null || mainMeth.getLevel() > 0) {
			report_error("Program nema korektnu main metodu", program);
		}
	}
	
	//Deklaracija konstantnih promenljivih 
	
	@Override
	public void visit(ConDecl_decl conDecl_decl) {
		Obj conObj = Tab.find(conDecl_decl.getI1());
		if(conObj != Tab.noObj) {
			report_error("Dvostruka deklaracija konstante: " + conDecl_decl.getI1(), conDecl_decl);
		}
		else {
			if(conType.assignableTo(currentType)) {
				conObj = Tab.insert(Obj.Con, conDecl_decl.getI1(), currentType);
				conObj.setAdr(constant);
			}
			else {
				report_error("Neadekvatna dodela konstanti: " + conDecl_decl.getI1(), conDecl_decl);
			}
		}
	}
	
	//Constant
	@Override
	public void visit(Constant_n constant_n) {
		constant = constant_n.getN1();
		conType = Tab.intType;
	}
	
	@Override
	public void visit(Constant_b constant_b) {
		constant = constant_b.getB1();
		conType = Tab.charType;
	}
	
	@Override
	public void visit(Constant_c constant_c) {
		constant = constant_c.getC1();
		conType = boolType;
	}
	
	//Type
	@Override
	public void visit(Type type) {
		Obj typeObj = Tab.find(type.getI1());
		if(typeObj == Tab.noObj) {
			report_error("Nepostojeci tip podatka [noObj]: " + type.getI1(), type);
			type.struct = currentType = Tab.noType;
		}
		else if(typeObj.getKind() != Obj.Type){
			report_error("Neadekvatan tip podatka: [noType] " + type.getI1(), type);
			type.struct = currentType = Tab.noType;
		}
		else {
			type.struct = currentType = typeObj.getType();
		}
	} 
	
	//Deklaracija varijabli
	
	@Override
	public void visit(VarDecl_ident varDecl_ident) {
		Obj varObj = null;
		if(currentMeth == null)
			varObj = Tab.find(varDecl_ident.getI1());
		else//vraca null ako ne postoji u opsegu
			varObj = Tab.currentScope().findSymbol(varDecl_ident.getI1());
		
		if(varObj == null || varObj == Tab.noObj) {//ako ga nema ni lokalno ni globalno dodajemo ga
			varObj = Tab.insert(Obj.Var, varDecl_ident.getI1(), currentType);
		}
		else {
			report_error("Dvostruka deklaracija varijable: " + varDecl_ident.getI1(), varDecl_ident);
		}//vec se nalazi u tabeli simbola ili kao globalna ili kao lokalna promenljiva
	}
	
	@Override
	public void visit(VarDecl_array varDecl_array) {
		Obj varObj = null;
		if(currentMeth == null)
			varObj = Tab.find(varDecl_array.getI1());
		else//vraca null ako ne postoji u opsegu
			varObj = Tab.currentScope().findSymbol(varDecl_array.getI1());
		
		if(varObj == null || varObj == Tab.noObj) {
			if(currentType == setType) {
				varObj = Tab.insert(Obj.Var, varDecl_array.getI1(), new Struct(Struct.Array, Tab.intType));
				//ako je currentType koji se procita setType onda cu u pozadini da napravim niz sa int elementima
			} else//a ako nije setType onda cu napraviti niz od currentType koji moze biti int, char,...
				varObj = Tab.insert(Obj.Var, varDecl_array.getI1(), new Struct(Struct.Array, currentType));
		}
		else {//kada dodajemo niz moramo da napravimo novi Struct cvor koji kaze da je on niz uz to da pise koji tip elemenata se nalazi u nizu
			report_error("Dvostruka deklaracija varijable [niza]: " + varDecl_array.getI1(), varDecl_array);
		}
	}
	
	//Obilazak metoda
	
	@Override
	public void visit(MethodTypeName_void methodTypeName_void) {
		methodTypeName_void.obj = currentMeth = Tab.insert(Obj.Meth, methodTypeName_void.getI1(), Tab.noType);
		Tab.openScope();
		
		if(methodTypeName_void.getI1().equalsIgnoreCase("main")) {
			mainMeth = currentMeth;
		}
	}
	
	@Override
	public void visit(MethodTypeName_type methodTypeName_type) {
		methodTypeName_type.obj = currentMeth = Tab.insert(Obj.Meth, methodTypeName_type.getI2(), currentType);
		Tab.openScope();
	}
	
	@Override
	public void visit(MethodDecl methodDecl) {
		Tab.chainLocalSymbols(currentMeth);
		Tab.closeScope();
		
		if(currentMeth.getType() != Tab.noType && !returnHappend) {
			report_error("Nije se desio return u metodi koja nije void: " + currentMeth.getName(), methodDecl);
		}
		
		currentMeth = null;
		returnHappend = false;
	}//svaki put kada se zavrsi metoda ona ce staviti returnHappend na false jer je ona zavrsila i ostavlja pocetno stanje za sl metodu
	
	//Formalni parametri metoda
	
	@Override
	public void visit(FormalParams_ident formalParams_ident) {
		Obj varObj = null;
		if(currentMeth == null)
			report_error("Semanticka greska [FormalParams_ident]", formalParams_ident);
		else//vraca null ako ne postoji u opsegu, a noObj ako postoji
			varObj = Tab.currentScope().findSymbol(formalParams_ident.getI2());
		
		if(varObj == null || varObj == Tab.noObj) {//ako ga nema ni lokalno ni globalno dodajemo ga
			varObj = Tab.insert(Obj.Var, formalParams_ident.getI2(), currentType);
			varObj.setFpPos(1);//postavljamo fpPos na 1 da kazemo da je to fParametar
			currentMeth.setLevel(currentMeth.getLevel() + 1);//uvecavamo broj formalnih parametara
		} else {//ako je varObj noObj znaci da vec postoji takav parametar u ovoj fji deklarisan
			report_error("Dvostruka deklaracija formalnog parametra f-je: " + formalParams_ident.getI2(), formalParams_ident);
		}
	}
	
	@Override
	public void visit(FormalParams_array formalParams_array) {
		Obj varObj = null;
		if(currentMeth == null)
			report_error("Semanticka greska [FormalParams_array]", formalParams_array);
		else//vraca null ako ne postoji u opsegu
			varObj = Tab.currentScope().findSymbol(formalParams_array.getI2());
		
		if(varObj == null || varObj == Tab.noObj) {//ako ga nema ni lokalno ni globalno dodajemo ga
			if(currentType == setType) {//ako je set zelim da ga sacuvam kao niz u mojoj metodi
				varObj = Tab.insert(Obj.Var, formalParams_array.getI2(), new Struct(Struct.Array, Tab.intType));
				varObj.setFpPos(1);//kazemo da je ovaj objekat u stvari formalni parametar f-je
				currentMeth.setLevel(currentMeth.getLevel() + 1);//uvecavamo broj formalnih parametara
			} else {
				varObj = Tab.insert(Obj.Var, formalParams_array.getI2(), new Struct(Struct.Array, currentType));
				varObj.setFpPos(1);//kazemo da je ovaj objekat u stvari formalni parametar f-je
				currentMeth.setLevel(currentMeth.getLevel() + 1);//uvecavamo broj formalnih parametara
			}
		}
		else {//ako je noObj onda znaci da je vec deklarisan
			report_error("Dvostruka deklaracija formalnog parametra f-je: " + formalParams_array.getI2(), formalParams_array);
		}
	}
	
	//Kontekstni uslovi
	
	
	//Designator
	@Override
	public void visit(Designator_ident designator_ident) {
		Obj varObj = Tab.find(designator_ident.getI1());
		if(varObj == Tab.noObj) {
			report_error("Pristup nedefinisanoj promenljivi: [Designator_ident]" + designator_ident.getI1(), designator_ident);
			designator_ident.obj = Tab.noObj;//nismo ga nasli tako da je on nista prazan
		}
		else if(varObj.getKind() != Obj.Var && varObj.getKind() != Obj.Con && varObj.getKind() != Obj.Meth) {//ako se 
			report_error("Neadekvatna promenljiva: " + designator_ident.getI1(), designator_ident);
			designator_ident.obj = Tab.noObj;
		}
		else {
			designator_ident.obj = varObj;
			if(varObj.getLevel() == 0 && varObj.getKind() == Obj.Var) {
				report_info("Pristup globalnoj promenljivoj: " + varObj.getName() + "[Kind: " + varObj.getKind() + "]", designator_ident);
			} else if (varObj.getLevel() != 0 && varObj.getKind() == Obj.Var){
				if(varObj.getFpPos() == 1) {
					report_info("Pristup formalnom parametru: " + varObj.getName() + "[Kind: " + varObj.getKind() + "]" + ", u funkciji " + currentMeth.getName(), designator_ident);
				} else {
					report_info("Pristup lokalnoj promenljivoj: " + varObj.getName() + "[Kind: " + varObj.getKind() + "]" + ", u funkciji " + currentMeth.getName(), designator_ident);
				}
			}
			if(varObj.getKind() == Obj.Con) {
				report_info("Pristup simbolickoj konstanti: " + varObj.getName() + "[Kind: " + varObj.getKind() + "]", designator_ident);
			}
		}
	}
	
	@Override
	public void visit(DesignatorArrName designatorArrName) {
		Obj arrObj = Tab.find(designatorArrName.getI1());
		if(arrObj == Tab.noObj) {
			report_error("Pristup nedefinisanoj promenljivi niza: [DesignatorArrName]" + designatorArrName.getI1(), designatorArrName);
			designatorArrName.obj = Tab.noObj;//nismo ga nasli tako da je on nista prazan
		}
		else if(arrObj.getKind() != Obj.Var || arrObj.getType().getKind() != Struct.Array) { 
			report_error("Neadekvatna promenljiva niza: " + designatorArrName.getI1(), designatorArrName);
			designatorArrName.obj = Tab.noObj;
		}
		else {
			designatorArrName.obj = arrObj;
		}
	}
	
	@Override
	public void visit(Designator_arr designator_arr) {
		Obj arrObj = designator_arr.getDesignatorArrName().obj;//iz sina uzimamo njegov objekat
		if(arrObj == Tab.noObj) {
			report_error("Pokusaj indeksiranja niza koji nije definisan! [Designator_arr]", designator_arr);
			designator_arr.obj = Tab.noObj;//ako nemamo objekat moramo da ga prosledimo na gore, a greska se ispisala preko sina vec
		} else if(!designator_arr.getExpr().struct.equals(Tab.intType)) {
			report_error("Indeksiranje niza sa vrednoscu koja nije int. [Designator_arr]", designator_arr);
			designator_arr.obj = Tab.noObj;
		} else if(arrObj.getType().getKind() != Struct.Array) {
			report_error("Pokusaj indeksiranje promenljive koja nije nizoskog tipa! [Designator_arr]", designator_arr);
			designator_arr.obj = Tab.noObj;
		} else { 
			designator_arr.obj = new Obj(Obj.Elem, arrObj.getName() + "[$]", arrObj.getType().getElemType());
			report_info("Pristup elementu niza: " + arrObj.getName() + "[Kind: " + designator_arr.obj.getKind() + "]", designator_arr);
		}//dodatni poeni ispis da li pristupamo el. niza
	}
	
	//FactorSub
	@Override
	public void visit(FactorSub_c factorSub_c) {
		factorSub_c.struct = Tab.charType;
	}
	
	@Override
	public void visit(FactorSub_n factorSub_n) {
		factorSub_n.struct = Tab.intType;
	}
	
	@Override
	public void visit(FactorSub_b factorSub_b) {
		factorSub_b.struct = boolType;
	}
	
	@Override
	public void visit(FactorSub_d factorSub_d) {
		factorSub_d.struct = factorSub_d.getDesignator().obj.getType();
	}

	@Override
	public void visit(FactorSub_arr factorSub_arr) {
		if(!factorSub_arr.getExpr().struct.equals(Tab.intType)) {
			report_error("Velicina niza nije tipa int.", factorSub_arr);
			factorSub_arr.struct = Tab.noType;
		} else {
			if(factorSub_arr.getType().struct.equals(setType)) {
				factorSub_arr.struct = setType;//ako je set stavljamo mu tip na set da bi kasnije znali sa cime imamo posla
			} else {
				factorSub_arr.struct = new Struct(Struct.Array, currentType);//tip uzimamo direktno iz deklaracije
			}
		}
	}
	
	@Override
	public void visit(FactorSub_z factorSub_z) {
		factorSub_z.struct = factorSub_z.getExpr().struct;
	}
	
	@Override
	public void visit(FactorSub_function factorSub_function) {
		if(factorSub_function.getDesignator().obj.getKind() != Obj.Meth) {
			report_error("Poziv neadekvatne medote: " + factorSub_function.getDesignator().obj.getName(), factorSub_function);
			factorSub_function.struct = Tab.noType;
		}
		else {
			factorSub_function.struct = factorSub_function.getDesignator().obj.getType();
		
			List<Struct> listaFParametara = new ArrayList<>();
			for(Obj lokalniParametri: factorSub_function.getDesignator().obj.getLocalSymbols()) {
				if(lokalniParametri.getKind() == Obj.Var && lokalniParametri.getLevel() == 1 && lokalniParametri.getFpPos() == 1) {
					listaFParametara.add(lokalniParametri.getType());//dodajemo sve tipove formalnih parametara
				}
			}
			
			//Pravimo proveru za sistemsku fje jer njihovi fParametri nemaju dobro namesten fpPos
			
			if(factorSub_function.getDesignator().obj.getName() == "chr" 
					|| factorSub_function.getDesignator().obj.getName() == "ord" 
					|| factorSub_function.getDesignator().obj.getName() == "len") {
				//brisemo sve iz liste sto je mozda ubaceno u nji
				listaFParametara.clear();
				for(Obj sisLokalni : factorSub_function.getDesignator().obj.getLocalSymbols()) {
					if(sisLokalni.getKind() == Obj.Var && sisLokalni.getLevel() == 1) {
						listaFParametara.add(sisLokalni.getType());
					}//prolazimo kroz sve formalne parametre sistemskih fja ali proveravam samo da li im je level dobar jer znamo da fpPos nije postavljen kako treba
				}
			}//sada u listi sigurno imam dobar broj parametara za bilo koju fju
			
			//Provera za fje isto sistemske
			if(factorSub_function.getDesignator().obj.getName() == "add") {
//				listaFParametara.clear();
//				listaFParametara.add(setType);
//				listaFParametara.add(Tab.intType);
			}//dodao sam u metode add i addAll njihove parametre i stavio sam im fpPos na 1 tako da ne moram posebnu proveru da radim
			
			if(factorSub_function.getDesignator().obj.getName() == "addAll") {
//				listaFParametara.clear();
//				listaFParametara.add(setType);
//				listaFParametara.add(new Struct(Struct.Array, Tab.intType));
			}
		
			
			ActParamsCounter apc = new ActParamsCounter();
			factorSub_function.getActParamsAppear().traverseBottomUp(apc);
			List<Struct> actp = apc.finalParamsList;
			try {
				if(listaFParametara.size() != actp.size()) {
					throw new Exception("Razlicit broj formalnih i pravih parametara");
				}
				for(int i = 0; i < listaFParametara.size(); i++) {
					Struct fpt = listaFParametara.get(i);
					Struct apt = actp.get(i);
					if(!apt.assignableTo(fpt)) {
						throw new Exception("Pogresni tipovi parametara");
					}
				}
			}
			catch(Exception e) {
				report_error("[" + e.getMessage() + "] " + "Nekompatibilni parametri za poziv metode: " + factorSub_function.getDesignator().obj.getName(), factorSub_function);
			}
		
		}
	}
	
	//Factor
	@Override
	public void visit(Factor_m factor_m) {
		if(factor_m.getFactorSub().struct.equals(Tab.intType)) {
			factor_m.struct = Tab.intType;
		} else {
			report_error("Negacija vrednosti koja nije int tip", factor_m);
			factor_m.struct = Tab.noType;//ako smo probali da negiramo nesto sto nije int taj Factor nam nije dobar i moramo mu dati tip noType
		}
	}
	
	@Override
	public void visit(Factor_p factor_p) {
		factor_p.struct = factor_p.getFactorSub().struct;//ako je pozitivan Factor ne proveravamo nista, on ce samo uzeti struct od factorSub-a koji mu se dodeli
	}
	
	//Expr
	@Override
	public void visit(MulFactorList_factor mulFactorList_factor) {
		mulFactorList_factor.struct = mulFactorList_factor.getFactor().struct;
	}//on samo prosto uzima tip od faktora koji se preslikao u njega iz gramatike
	
	@Override
	public void visit(MulFactorList_mul mulFactorList_mul) {
		Struct left = mulFactorList_mul.getMulFactorList().struct;
		Struct right = mulFactorList_mul.getFactor().struct;
		if(left.equals(Tab.intType) && right.equals(Tab.intType)) {
			mulFactorList_mul.struct = Tab.intType;
		} else {
			report_error("Mul operacija ne int promenljivih vrednosti", mulFactorList_mul);
			mulFactorList_mul.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(AddTermList_term addTermList_term) {
		addTermList_term.struct = addTermList_term.getTerm().struct;
	}//samo prosledjujemo tip terma u koji se preslikala addTermList_term
	
	@Override
	public void visit(AddTermList_add addTermList_add) {
		Struct left = addTermList_add.getAddTermList().struct;
		Struct right = addTermList_add.getTerm().struct;
		if(left.equals(Tab.intType) && right.equals(Tab.intType)) {
			addTermList_add.struct = Tab.intType;
		} else {
			report_error("Add operacija ne int promenljivih vrednosti", addTermList_add);
			addTermList_add.struct = Tab.noType;
		}
	}

	@Override
	public void visit(Term term) {
		term.struct = term.getMulFactorList().struct;
	}
	
	@Override
	public void visit(Expr_add expr_add) {
		expr_add.struct = expr_add.getAddTermList().struct;
	}
	
	@Override
	public void visit(Expr_map expr_map) {
		Obj levi = expr_map.getDesignator().obj;
		if(levi.getKind() != Obj.Meth) {
			report_error("Levi designator nije tipa metode: " + levi.getName(), expr_map);
			expr_map.struct = Tab.noType;
		} else {
			if(levi.getType().getKind() != Struct.Int) {
				report_error("Povratna vrednost nije int vrednost.", expr_map);
				expr_map.struct = Tab.noType;
			} else {
				int cnt = 0;
				for(Obj el: levi.getLocalSymbols()) {//ako je lista prazna nece se uci u for petlju
					cnt++;
					if(el.getKind() == Obj.Var && el.getLevel() == 1 && el.getFpPos() == 1) {
						if(cnt == 1 && el.getType().getKind() == Struct.Int) {
							cnt++;
						} else {
							report_error("Funkcija ima vise od jednog parametra.", expr_map);
							expr_map.struct = Tab.noType;
						}
					}
				}
				if(cnt == 0) {
					report_error("Pozvana fja nema nijedan parametar, a mora da ima jedan tipa int.", expr_map);
				}
				Obj desni = expr_map.getDesignator1().obj;
				if(desni.getType().getKind() != Struct.Array || desni.getType().getElemType().getKind() != Struct.Int) {
					report_error("Desni designator nije nizovskog tipa ili njegovi elementi nisu tipa int.", expr_map);
					expr_map.struct = Tab.noType;
				}
			}
		}
		expr_map.struct = Tab.intType;
	}
	
	//DesignatorStatements
	@Override
	public void visit(DesignatorStatement_ass designatorStatement_ass) {
		int kind = designatorStatement_ass.getDesignator().obj.getKind();
		if(kind != Obj.Var && kind != Obj.Elem) {
			report_error("Dodela u neadekvatnu promenljivu: " + designatorStatement_ass.getDesignator().obj.getName(), designatorStatement_ass);
		} else if(!designatorStatement_ass.getExpr().struct.assignableTo(designatorStatement_ass.getDesignator().obj.getType())) {
			report_error("Dodela pogresnog tipa u promenljivu: " + designatorStatement_ass.getDesignator().obj.getName(), designatorStatement_ass);
		}
	}
	
	@Override
	public void visit(DesignatorStatement_inc designatorStatement_inc) {
		int kind = designatorStatement_inc.getDesignator().obj.getKind();
		if(kind != Obj.Var && kind != Obj.Elem) {
			report_error("Neadekvatan promenljiva za inkrementiranje: " + designatorStatement_inc.getDesignator().obj.getName(), designatorStatement_inc);
		} else if(!designatorStatement_inc.getDesignator().obj.getType().equals(Tab.intType)) {
			report_error("Neadekvatan tip za inkrementiranje: " + designatorStatement_inc.getDesignator().obj.getName(), designatorStatement_inc);
		}
	}

	@Override
	public void visit(DesignatorStatement_dec designatorStatement_dec) {
		int kind = designatorStatement_dec.getDesignator().obj.getKind();
		if(kind != Obj.Var && kind != Obj.Elem) {
			report_error("Neadekvatan promenljiva za dekrementiranje: " + designatorStatement_dec.getDesignator().obj.getName(), designatorStatement_dec);
		} else if(!designatorStatement_dec.getDesignator().obj.getType().equals(Tab.intType)) {
			report_error("Neadekvatan tip za dekrementiranje: " + designatorStatement_dec.getDesignator().obj.getName(), designatorStatement_dec);
		}
	}
	
	@Override//poziv metode
	public void visit(DesignatorStatement_actpar designatorStatement_actpar) {
		if(designatorStatement_actpar.getDesignator().obj.getKind() != Obj.Meth) {
			report_error("Poziv neadekvatne metode: " + designatorStatement_actpar.getDesignator().obj.getName(), designatorStatement_actpar);
		} else {
			List<Struct> listaFParametara = new ArrayList<>();
			for(Obj lokalniParametri: designatorStatement_actpar.getDesignator().obj.getLocalSymbols()) {
				if(lokalniParametri.getKind() == Obj.Var && lokalniParametri.getLevel() == 1 && lokalniParametri.getFpPos() == 1) {
					listaFParametara.add(lokalniParametri.getType());//dodajemo sve tipove formalnih parametara
				}
			}
			
			//Funkcije koje prave problem su chr, ord i len zato sto iz nekog razloga njihovim fParametrima fpPos nije postavljen na 1 i moram da uradim dodatnu proveru ako se radi o tim f-ma
			
			if(designatorStatement_actpar.getDesignator().obj.getName() == "chr" 
					|| designatorStatement_actpar.getDesignator().obj.getName() == "ord" 
					|| designatorStatement_actpar.getDesignator().obj.getName() == "len") {
				//brisemo sve iz liste sto je mozda ubaceno u nji
				listaFParametara.clear();
				for(Obj sisLokalni : designatorStatement_actpar.getDesignator().obj.getLocalSymbols()) {
					if(sisLokalni.getKind() == Obj.Var && sisLokalni.getLevel() == 1) {
						listaFParametara.add(sisLokalni.getType());
					}//prolazimo kroz sve formalne parametre sistemskih fja ali proveravam samo da li im je level dobar jer znamo da fpPos nije postavljen kako treba
				}
			}//sada u listi sigurno imam dobar broj parametara za bilo koju fju
			
//			if(designatorStatement_actpar.getDesignator().obj.getName() == "add") {
//				listaFParametara.clear();
//				listaFParametara.add(setType);
//				listaFParametara.add(Tab.intType);
//			}
			
//			if(designatorStatement_actpar.getDesignator().obj.getName() == "addAll") {
//				listaFParametara.clear();
//				listaFParametara.add(setType);
//				listaFParametara.add(new Struct(Struct.Array, Tab.intType));
//			}
			
			ActParamsCounter apc = new ActParamsCounter();
			designatorStatement_actpar.getActParamsAppear().traverseBottomUp(apc);
			List<Struct> actp = apc.finalParamsList;
			
			
			try {
				if(listaFParametara.size() != actp.size()) {
					throw new Exception("Razlicit broj formalnih i pravih parametara");
				}
				for(int i = 0; i < listaFParametara.size(); i++) {
					Struct fpt = listaFParametara.get(i);
					Struct apt = actp.get(i);
					if(!apt.assignableTo(fpt)) {
						throw new Exception("Pogresni tipovi parametara");
					}
				}
			}
			catch(Exception e) {
				report_error("[" + e.getMessage() + "] " + "Nekompatibilni parametri za poziv metode: " + designatorStatement_actpar.getDesignator().obj.getName(), designatorStatement_actpar);
			}
		}
		report_info("Poziv globalne fje: " + designatorStatement_actpar.getDesignator().obj.getName() + "[Kind: " + designatorStatement_actpar.getDesignator().obj.getKind() + "]", designatorStatement_actpar);
	}
	
	@Override
	public void visit(DesignatorStatement_setop designatorStatement_setop) {
		Struct prvi = designatorStatement_setop.getDesignator().obj.getType();
		Struct drugi = designatorStatement_setop.getDesignator1().obj.getType();
		Struct treci = designatorStatement_setop.getDesignator2().obj.getType();
		if(!prvi.equals(setType) || !drugi.equals(setType) || !treci.equals(setType)) {
			report_error("Poziv setovske opracije unija nad ne setovskim tipovima.", designatorStatement_setop);
		}
	}
	
	//Statemetns
	
	@Override
	public void visit(Statement_read statement_read) {
		int kind = statement_read.getDesignator().obj.getKind();
		Struct desType = statement_read.getDesignator().obj.getType();
		if(kind != Obj.Var && kind != Obj.Elem) {
			report_error("Neadekvatna promenljiva za read operaciju: " + statement_read.getDesignator().obj.getName(), statement_read);
		} else if(!desType.equals(Tab.intType) && !desType.equals(boolType) && !desType.equals(Tab.charType)) {
			report_error("Neadekvatan tip promenljive za read operaciju: " + statement_read.getDesignator().obj.getName(), statement_read);
		}
	}
	
	@Override
	public void visit(Statement_printe statement_printe) {
		Struct kind = statement_printe.getExpr().struct;
		if(kind.equals(Tab.intType) && kind.equals(Tab.charType) && kind.equals(boolType) && kind.equals(setType)) {
			report_error("Neadekvatan tip izraza za print operaciju.", statement_printe);
		}
	}
	
	@Override
	public void visit(Statement_printen statement_printen) {
		Struct kind = statement_printen.getExpr().struct;
		if(kind.equals(Tab.intType) && kind.equals(Tab.charType) && kind.equals(boolType) && kind.equals(setType)) {
			report_error("Neadekvatan tip izraza za print operaciju.", statement_printen);
		}
	}
	
	@Override
	public void visit(Statement_return statement_return) {
		returnHappend = true;
		if(currentMeth == null) {
			report_error("Return statement pozvan van metode.", statement_return);
		}
		if(currentMeth.getType() != Tab.noType) {
			report_error("Return iskaz je tipa void u ne void metodi: " + currentMeth.getName(), statement_return);
		}
	}
	
	@Override
	public void visit(Statement_returnex statement_returnex) {
		returnHappend = true;
		if(currentMeth == null) {
			report_error("Return statemetn pozvan van metode.", statement_returnex);
		}
		if(!currentMeth.getType().equals(statement_returnex.getExpr().struct)) {
			report_error("Return iskaz nije istog tipa kao povratna vrednost metode: " + currentMeth.getName(), statement_returnex);
		}
	}
	
	@Override
	public void visit(DoNonterminal doNonterminal) {
		cntDo++;
	}
	
	@Override
	public void visit(Statemetn_do statement_do) {
		cntDo--;
	}
	
//	@Override
//	public void visit(ForNonTerminal forNonTerminal) {
//		cntDo++;
//	}
//	
//	@Override
//	public void visit(Statement_for statement_for) {
//		cntDo--;
//	}
	
	@Override
	public void visit(Statement_break statement_break) {
		if(cntDo == 0) {
			report_error("Pokusaj break naredbe van do-while petlje", statement_break);
		}
	}
	
	@Override
	public void visit(Statement_continue statement_continue) {
		if(cntDo == 0) {
			report_error("Pokusaj continue naredbe van do-while petlje", statement_continue);
		}
	}
	
	//Condition
	@Override
	public void visit(CondFact_single condFact_single) {
		if(!condFact_single.getExpr().struct.equals(boolType)) {
			report_error("Logicki operand nije tipa bool.", condFact_single);
			condFact_single.struct = Tab.noType;
		} else {
			condFact_single.struct = boolType;
		}
	}
	
	@Override
	public void visit(CondFact_relopExpr condFact_relopExpr) {
		Struct left = condFact_relopExpr.getExpr().struct;
		Struct right = condFact_relopExpr.getExpr1().struct;
		if(!left.compatibleWith(right)) {
			report_error("Operandi nisu kompatibilnih tipova.", condFact_relopExpr);
			condFact_relopExpr.struct = Tab.noType;
		} else {
			if(left.isRefType() || right.isRefType()) {
				if(condFact_relopExpr.getRelop() instanceof Relop_eq || condFact_relopExpr.getRelop() instanceof Relop_neq) {
					condFact_relopExpr.struct = boolType; 
				} else {
					report_error("Poredjenje ref tipova sa losim relacionim operatorom.", condFact_relopExpr);
					condFact_relopExpr.struct = Tab.noType;
				}
			} else 
				condFact_relopExpr.struct = boolType; 
		}
	}
	
	@Override
	public void visit(CondFactList_cond condFactList_cond) {
		condFactList_cond.struct = condFactList_cond.getCondFact().struct;
	}
	
	@Override
	public void visit(CondFactList_rek condFactList_rek) {
		Struct left = condFactList_rek.getCondFactList().struct;
		Struct right = condFactList_rek.getCondFact().struct;
		if(!left.equals(boolType) || !right.equals(boolType)) {
			report_error("Operandi AND operacije nisu tipa bool.", condFactList_rek);
			condFactList_rek.struct = Tab.noType;
		} else {
			condFactList_rek.struct = boolType;
		}
	}
	
	@Override
	public void visit(CondTerm condTerm) {
		condTerm.struct = condTerm.getCondFactList().struct;
	}
	
	@Override
	public void visit(CondTermList_cond condTermList_cond) {
		if(!condTermList_cond.getCondTerm().struct.equals(boolType)) {
			report_error("Logicki operand nije tipa bool.", condTermList_cond);
			condTermList_cond.struct = Tab.noType;
		} else {
			condTermList_cond.struct = boolType;
		}
	}
	
	@Override
	public void visit(CondTermList_rek condTermList_rek) {
		Struct left = condTermList_rek.getCondTermList().struct;
		Struct right = condTermList_rek.getCondTerm().struct;
		if(!left.equals(boolType) || !right.equals(boolType)) {
			report_error("Operandi OR operacije nisu tipa bool.", condTermList_rek);
			condTermList_rek.struct = Tab.noType;
		} else {
			condTermList_rek.struct = boolType;
		}
	}
	
	@Override
	public void visit(Condition_list condition_list) {
		condition_list.struct = condition_list.getCondTermList().struct;
		if(!condition_list.struct.equals(boolType)) {
			report_error("Condition nije tipa bool!", condition_list);
		}
	}
	
}
