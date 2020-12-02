package package1;

import java.util.ArrayList;
import java.util.Random;

/*
 * This class implements array list to store the data
 * It also has some basic methods needed for the algorithm
 */
public class SATData {
	ArrayList<Boolean> variables;
	ArrayList<ArrayList<Integer>> clauses;
	ArrayList<Integer> unsatisfiedClause;
	int size;
	int index;
	
	public SATData(int n) {
		size = n;
		index = 0;
		variables = new ArrayList<Boolean>(n);
		for(int i = 0; i < n; i++) {
			variables.add(false); // initialize variables
		}
		clauses = new ArrayList<ArrayList<Integer>>();
		unsatisfiedClause = new ArrayList<Integer>(2); // update when checkAssignment
		unsatisfiedClause.add(0);
		unsatisfiedClause.add(1);		
	}
	
	public void addClause(ArrayList<Integer> a) {
		clauses.add(a);
	}
	
	public boolean findUnsatisfied(){
		// it finds the next unsatisfied clause
		int count = 0;
		while(count < size) {
			if(index >= size) {
				index = 0; // after all the index, reset it
			}
			int first = clauses.get(index).get(0);
			int second = clauses.get(index).get(1);
			if(!check(first, second)) {
				unsatisfiedClause.set(0, first); // store the unsatisfied clause
				unsatisfiedClause.set(1, second);
				index++;
				return true;
			}
			index++;
			count++;
		}
		return false; // false means no unsatisfied clause
	}
	
	public boolean checkAssignment() {
		// return false unless all clauses are satisfied
		for(ArrayList<Integer> clause : clauses) {
			int first = clause.get(0);
			int second = clause.get(1);
			if(!check(first, second)) {
				unsatisfiedClause.set(0, first); // store the unsatisfied clause
				unsatisfiedClause.set(1, second);
				return false;
			}
		}
		return true;
	}
	
	public boolean check(int first, int second) {
		/*
		 * first value is the abs(index) + 1, positive --> true, negative --> false
		 * same for the second
		 * the clause is satisfied if one of them is true
		 */
		if(first > 0) {
			if(variables.get(first - 1)) {
				return true;
			}
		}
		else {
			if(!variables.get(-first - 1)) {
				return true;
			}
		}
		if(second > 0) {
			if(variables.get(second - 1)) {
				return true;
			}
		}
		else {
			if(!variables.get(-second - 1)) {
				return true;
			}
		}
		return false;
	}
	
	public void randomAssign() {
		Random rand = new Random();
		for(int i = 0; i < size; i++) {
			if(rand.nextBoolean()) {
				variables.set(i, true);
			}
			else {
				variables.set(i, false);
			}
		}
	}

}
