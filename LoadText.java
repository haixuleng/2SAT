package package1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadText {
	String fileToLoad;
	int length = 0;
	// First line of the file stores the number of vertexes from the test file
	public LoadText(String fileName) throws FileNotFoundException {
		fileToLoad = fileName;
		System.out.println(fileName);
		size();
	}

	public void size() throws FileNotFoundException {
		int size = 0;
		File myObj = new File(fileToLoad);
		Scanner myReader = new Scanner(myObj);
		while(myReader.hasNextLine()) {
			size ++;
			myReader.nextLine();
			//System.out.println("Length of Input: " + size);
		}
		//myReader.close();
		length = size;
		System.out.println("Length of Input: " + size);
	}

	public SATData get() throws FileNotFoundException {
		File myObj = new File(fileToLoad);
		Scanner myReader = new Scanner(myObj);
		SATData initData = new SATData(length - 1);
		int index = 0;
		while(myReader.hasNextLine()) {
			String data = myReader.nextLine().strip();
			if(index < 1) {
				index ++;
				continue; // starting line
			}
			String[] values = data.split("\\s");
			ArrayList<Integer> c = new ArrayList<Integer>();
			c.add(Integer.parseInt(values[0]));
			c.add(Integer.parseInt(values[1]));
			initData.addClause(c);
		}
		myReader.close();
		return initData;
	}
}
