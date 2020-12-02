package package1;

import java.io.FileNotFoundException;

public class Papadimitriou {
	public static void main(String args[]) throws FileNotFoundException {
		LoadText lt = new LoadText("data/input_beaunus_28_4000.txt");
		SATData d = lt.get();
		System.out.println("Data size: " + d.size);
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
