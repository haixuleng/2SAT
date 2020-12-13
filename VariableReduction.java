package package1;

import java.util.ArrayList;
import java.util.HashMap;
/*
 * There is a method to reduce the number of pairs so the algorithm can run really fast. And after thinking about it here is my proof. It's not strict and professional but I hope it can help you with this issue.

So basically the reduction method runs for many iterations, and in every iteration it removes those pairs that contains a verb that are all negated or not negated.

for instance,

you have pairs

(-x1,x2), (x1,x2),(-x1,x3),(-x3,x4),(x3,-x4),(-x1,x5),(x2,x6) and since x2 here are always non-negated, so remove the pairs contain x2 we get

(-x1,x3),(-x3,x4),(x3,-x4),(-x1,x5)

And notice that although in original set x1 has both negated and non-negated form, after the removal of x2 now x1 has only negated form, which is in (-x1,x3) and (-x1,x5), so remove this one we get

(-x3,x4),(x3,-x4)

Now this can't be reduced anymore. let's call it core 2-SAT. And this is easily satisfied with x3 = True and x4 = True.

Now the question is, can we say that if the reduced 2-SAT can be satisfied, will the original 2-SAT also be satisfied?
 */

public class VariableReduction {
	ArrayList<ArrayList<Integer>> variables;
	Boolean clauseRecord[];
	ArrayList<ArrayList<Integer>> clauses;
	public VariableReduction(SATData d) {
		int n = d.size;
		variables = new ArrayList<ArrayList<Integer>>(n);
		clauseRecord = new Boolean[n];
		clauses = d.clauses;
		init();
		System.out.println("Before checking: " + checkRecord() + " are true");
		countClause(); // count how many clauses each variable has
		setRecord();
		System.out.println("After checking: " + checkRecord() + " are true");
	}
	
	private void init() {
		// all clauses are meaningful at the beginning.
		ArrayList<Integer> emp = new ArrayList<Integer>();
		emp.add(-9999);
		for(int i = 0; i < clauses.size(); i++) {
			clauseRecord[i] = true;
			variables.add(emp);
		}
	}
	
	public void refreshVariable() {
		ArrayList<Integer> emp = new ArrayList<Integer>();
		emp.add(-9999);
		for(int i = 0; i < clauses.size(); i++) {
			variables.set(i, emp);
		}
	}
	
	public void countClause() {
		// count how many clauses each variable has
		for(int i = 0; i < clauses.size(); i++) {
			if(!clauseRecord[i]) {
				// no need to count useless clauses
				continue;
			}
			int first = Math.abs(clauses.get(i).get(0)) - 1;
			int second = Math.abs(clauses.get(i).get(1)) - 1;
			// first variable of the clause
			if(variables.get(first).get(0) < 0) {
				ArrayList<Integer> cl = new ArrayList<Integer>();
				cl.add(i);
				variables.set(first, cl);
			}
			else {
				ArrayList<Integer> cl = variables.get(first);
				cl.add(i);
				variables.set(first, cl);
			}
			// second variable of the clause
			if(variables.get(second).get(0) < 0) {
				ArrayList<Integer> cl = new ArrayList<Integer>();
				cl.add(i);
				variables.set(second, cl);
			}
			else {
				ArrayList<Integer> cl = variables.get(second);
				cl.add(i);
				variables.set(second, cl);
			}
		}
		//print();
	}
	
	public void print() {
		for(ArrayList<Integer> varList : variables) {
			for(int i: varList) {
				System.out.print(i + " ");
			}
			System.out.println(" ");
		}
	}
	
	public void setRecord() {
		// set the clause record to false if one variable can be arbitrarily set
		//System.out.println(variables.get(0));
		//int variableIndex = 11;
		for(int variableIndex = 0; variableIndex < variables.size(); variableIndex++) {
			//System.out.println(a.get(0));
			ArrayList<Integer> a = variables.get(variableIndex);
			if(a.size() <= 1) {
				// a variable only has one clause
				int clauseIndex = a.get(0);
				if(clauseIndex < 0) {
					continue;
				}
				clauseRecord[clauseIndex] = false;
			}
			else{
				if(checkSameSign(variableIndex)) {
					for(int i: a) {
						clauseRecord[i] = false;
					}
				}
			}
			//variableIndex++;
		}
	}
	
	public boolean checkSameSign(int variableIndex) {
		ArrayList<Integer> clauseList = variables.get(variableIndex);
		int previousSign = 0;
		int currentSign = 1;
		for(int clauseIndex: clauseList) {
			//System.out.println("Index: " + variableIndex + "," + clauseIndex + "," + clauseList.size());
			int firstVar = clauses.get(clauseIndex).get(0);
			int secondVar = clauses.get(clauseIndex).get(1);
			
			int absFirstVar = Math.abs(firstVar) - 1;
			int absSecondVar = Math.abs(secondVar) - 1;
			if(variableIndex == absFirstVar) {
				if(firstVar > 0) {
					currentSign = 1;
				}
				else {
					currentSign = -1;
				}
			}
			else {
				if(secondVar > 0) {
					currentSign = 1;
				}
				else {
					currentSign = -1;
				}
			}
			if(previousSign*currentSign < 0) {
				return false;
			}
			else {
				previousSign = currentSign;
			}
		}
		return true;
	}
	
	public int checkRecord() {
		// return how many clauses are valid
		int n = 0;
		for(int i = 0; i < clauses.size(); i++) {
			if(clauseRecord[i]) {
				n++;
			}
		}
		return n;
	}
	
	public SATData get() {
		// return the reduced clauses
		SATData initData = new SATData(checkRecord());
		int index = 1; // new index for the variables
		HashMap<Integer, Integer> newIndexMap = new HashMap<Integer, Integer>();
		int newFirstVar = 1;
		int newSecondVar = 1;
		for(int i = 0; i < clauses.size(); i++) {
			if(clauseRecord[i]) {
				// a valid clause
				ArrayList<Integer> clause = clauses.get(i);
				int firstVar = clause.get(0);
				int secondVar = clause.get(1);
				int absFirstVar = Math.abs(firstVar);
				int absSecondVar = Math.abs(secondVar);
				
				if(newIndexMap.containsKey(absFirstVar)) {
					if(firstVar > 0) {
						newFirstVar = newIndexMap.get(absFirstVar);
					}
					else {
						newFirstVar = newIndexMap.get(absFirstVar) * (-1);
					}
				}
				else {
					// assign a new index to the new clause
					if(firstVar > 0) {
						newFirstVar = index;
					}
					else {
						newFirstVar = index * (-1);
					}
					newIndexMap.put(absFirstVar, index); // update the hash map
					index++;
				}
				
				if(newIndexMap.containsKey(absSecondVar)) {
					if(secondVar > 0) {
						newSecondVar = newIndexMap.get(absSecondVar);
					}
					else {
						newSecondVar = newIndexMap.get(absSecondVar) * (-1);
					}
				}
				else {
					// assign a new index to the new clause
					if(secondVar > 0) {
						newSecondVar = index;
					}
					else {
						newSecondVar = index * (-1);
					}
					newIndexMap.put(absSecondVar, index); // update the hash map
					index++;
				}
				ArrayList<Integer> c = new ArrayList<Integer>();
				c.add(newFirstVar);
				c.add(newSecondVar);
				initData.addClause(c);	
			}
		}
		System.out.println("New clauses genearted with size:" + initData.size);
		return initData;
	}
}