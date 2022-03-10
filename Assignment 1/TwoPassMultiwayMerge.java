/**
 * @author Tavtej Singh, Kanishak Kapur
 * This is the implementation for 2 phase multiway merge sort for Assignment 1 
 * of COMP 6521 - Advance Database
 */

package comp6521_assignment1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TwoPassMultiwayMerge {
	
	List<List<Integer>> sortedLists;
	int memory;
	
	/* Task 1*/
	public void generateRandomInts(String fileName, int n)  {
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fileName);
			PrintWriter print = new PrintWriter(fos);
			
			Random random = new Random();
			
			for(int i=0; i<n; i++) {
				int rand = random.nextInt(100000);
				print.println(rand);
			}
			print.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/* Display random integer list */
	public void display() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("input.txt"));
			String line;
            while ((line = br.readLine()) != null) {
                System.out.print(Integer.parseInt(line) + " ");
            }
            System.out.println();
            br.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("No input.txt exists. Select option 1 first");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* First Phase of 2PMMS*/
	public void firstPhase(int m, int n) throws NumberFormatException, IOException {
		this.memory = m;
		sortedLists = new ArrayList<>();
		System.out.println("Phase1:" + "\n----------");
		
		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		int numOfFiles = (n%m == 0) ? n/m : n/m +1 ;
		for(int i=1;i<=numOfFiles;i++) {
			List<Integer> lines = new ArrayList<Integer>();
			int j = 0;
	        while (j<m) {
	        	String line = br.readLine();
	        	if(line == null) break;
	            lines.add(Integer.parseInt(line));
	            j++;
	        }
	       
	        System.out.print("Unsorted List "+i+" :: ");
	        for(Integer num : lines) {
	        	System.out.print(num+" ");
	        }
	        System.out.println();
	        Collections.sort(lines);
	        System.out.print("Sorted List "+i+" :: ");
	        for(Integer num : lines) {
	        	System.out.print(num+" ");
	        }
	        System.out.println();
	        sortedLists.add(lines);
		}
		br.close();
	}
	
	/* Second Phase of 2PMMS*/
	public void secondPhase() {
		System.out.println("Phase2:" + "\n----------");	
		int pass = 1;
		while(sortedLists.size()!=1) {
			System.out.println("Pass "+pass+" :: ");
			pass++;
			int readSize = Math.min(sortedLists.size(), memory-1); //will choose the block of size = readSize
			List<List<Integer>> intermediateList = new ArrayList<>();
			
			for(int i=0;i<sortedLists.size();) {
				List<List<Integer>> blocks = new ArrayList<>();
				int start = i+1;
				for(int j=0;j<readSize&&i<sortedLists.size();j++) {
					blocks.add(sortedLists.get(i));
					i++;
				}
				System.out.println("Merging lists from List-"+start+" to List-"+i);
				intermediateList.add(mergeKLists(blocks));
			}
			
			sortedLists = intermediateList;
		}
		
		/* Display final list*/
		List<Integer> ans = sortedLists.get(0);
		System.out.println("Final list::");
		for(Integer num : ans) {
			System.out.print(num +" ");
		}
		System.out.println();
	}
	
	private List<Integer> mergeKLists(List<List<Integer>> blocks){
		if(blocks.size() == 1) {
			return blocks.get(0);
		}
		List<Integer> finalList = new ArrayList<>();
		
		while(true) {
			int minVal = Integer.MAX_VALUE;
			int minIndex = -1;
			int emptyList = 0;
			for(int i=0;i<blocks.size();i++) {
				if(blocks.get(i).size()==0) {
					emptyList++;
				}
				else if(blocks.get(i).get(0)<minVal) {
					minVal = blocks.get(i).get(0);
					minIndex = i;
				}
			}
			if(emptyList == blocks.size()) {
				break;
			}
			finalList.add(minVal);
			blocks.get(minIndex).remove(0);
		}
		
		return finalList;
	}
}
