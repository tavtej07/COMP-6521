import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(new FileReader("input.txt"));
			List<int[]> records = new ArrayList<>();
			
			while(sc.hasNext()) {
				int attr1 = sc.nextInt();
				int attr2 = sc.nextInt();
				int[] record = new int[] {attr1,attr2};
				records.add(record);
			}
			
			int[][] points = new int[records.size()][2];
			for(int i=0;i<records.size();i++) {
				points[i] = records.get(i);
			}
			
			KDTree tree = new KDTree();
			tree.buildTree(points);
			tree.displayTree();
			System.out.println("===========================================================");
			int choice = -1;
			sc = new Scanner(System.in);
			
			while(choice!= 3) {
				System.out.println("Enter operation:"+"\n"+"Type 1 to insert record"+ "\n"+ "Type 2 to delete record"+"\n"+"Type 3 to exit");
				choice = sc.nextInt();
				switch(choice){
				 case 1: 
					 int[] insertRec = new int[2];
					 System.out.println("Enter age :");
					 insertRec[0] = sc.nextInt();
					 System.out.println("Enter salary :");
					 insertRec[1] = sc.nextInt();
					 tree.insert(insertRec);
					 tree.displayTree();
					 System.out.println("===========================================================");
					 break;
					 
				 case 2: 
					 int[] delRec = new int[2];
					 System.out.println("Enter age :");
					 delRec[0] = sc.nextInt();
					 System.out.println("Enter salary :");
					 delRec[1] = sc.nextInt();
					 tree.delete(delRec);
					 tree.displayTree();
					 System.out.println("===========================================================");
					 break;
					 
				 case 3:
					 break;
					 
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
