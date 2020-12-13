package package1;

import java.io.FileNotFoundException;

public class test {
	public static void main(String args[]) throws FileNotFoundException {
		//LoadText lt = new LoadText("data/input_beaunus_37_80000.txt");
		LoadText lt = new LoadText("data/2sat6.txt");
		SATData initD = lt.get();
		System.out.println("Data size: " + initD.size);
		//ClauseReduction cr = new ClauseReduction(d);
		VariableReduction cr = new VariableReduction(initD);
		int loopNum = 1000;
		int loop = loopNum;
		int minClause = cr.checkRecord();
		while(loopNum > 0) {
			cr.refreshVariable();
			cr.countClause(); // count how many clauses each variable has
			cr.setRecord();
			if(cr.checkRecord() < minClause) {
				minClause = cr.checkRecord();
			}
			else {
				break;
			}
			loopNum--;
		}
		System.out.println("After checking " + (loop - loopNum + 1) + " times: " + cr.checkRecord() + " are true");
		SATData d = cr.get();
		
		/*
		 * Start to run Papadimitriou flow
		 */
		boolean finished = false;
		for(int outerLoop = 0; outerLoop < Math.log(d.size); outerLoop++) {
			d.randomAssign(); // randomly assign values
			int innerLoop = 2 * d.size * d.size;
			if(finished) {
				break;
			}
			while(innerLoop > 0) {
				if(!d.findUnsatisfied()) {
					System.out.println("Finished !");
					finished = true;
					break;
				}
				else {
					int first = d.unsatisfiedClause.get(0);
					int second = d.unsatisfiedClause.get(1);
					if(Math.random() < 0.5) {
						// flip the first
						d.variables.set(Math.abs(first) - 1, !d.variables.get(Math.abs(first) - 1));
					}
					else {
						// flip the second
						d.variables.set(Math.abs(second) - 1, !d.variables.get(Math.abs(second) - 1));						
					}
				}
				innerLoop--;
				if(innerLoop % 10000 == 0) {
					System.out.println("Innerloop finished:" + (2* d.size * d.size - innerLoop) + "/" + (2* d.size * d.size));
				}
			}
		}
		if(!finished) {
			System.out.println("Not found!");
		}
	}

}
