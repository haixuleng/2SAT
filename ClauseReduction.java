package package1;

import java.util.ArrayList;

/*
 * The idea is that there are too many variables and clauses. But only few of them
 * are meaningful. For example, if one clause relates to variable m and n, and n
 * is only represented by this clause. Then we can make n whatever needed to satisfy
 * this clause. And this specific clause is removed from the pool.
 * 
 * This class reduces the huge variables/clauses into a small but meaningful set.
 */
public class ClauseReduction {
	ArrayList<ArrayList<Integer>> variables;
	Boolean clauseRecord[];
	ArrayList<ArrayList<Integer>> clauses;
	public ClauseReduction(SATData d) {
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
	}
	
	public void setRecord() {
		// set the clause record to false if one variable can be arbitrarily set
		//System.out.println(variables.get(0));
		for(ArrayList<Integer> a : variables) {
			//System.out.println(a.get(0));
			if(a.size() == 1) {
				// a variable only has one clause
				int clauseIndex = a.get(0);
				if(clauseIndex < 0) {
					continue;
				}
				clauseRecord[clauseIndex] = false;
			}
		}
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
}
