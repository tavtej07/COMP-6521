	import java.util.Arrays;

class InteriorNode{
	
	//If leaf node
	int index ;
	int[][] block;
	boolean isLeaf;
	
	
	// If Interior node
	int criteria;
	int splitValue;
	
	InteriorNode left;
	InteriorNode right;
	
	InteriorNode(){
		isLeaf = true;
		block = new int[2][];
		block[0] = new int[] {Integer.MIN_VALUE,Integer.MIN_VALUE};
		block[1] = new int[] {Integer.MIN_VALUE,Integer.MIN_VALUE};
		index = 0;
		
	}
	
	InteriorNode(int cr, int split){
		criteria = cr;
		splitValue = split;
		isLeaf = false;
	}
	
	// Insert record in block
	void insertRecord(int[] record) {
		block[index] = record;
		index++;
	}
	
	public String printBlock() {
		StringBuilder sb = new StringBuilder("");
		for(int i=0;i<index;i++) {
			sb.append("["+block[i][0]+","+block[i][1]+"]");
		}
		return sb.toString();
	}
	
	public String toString() {
		if(isLeaf) {
			return printBlock();
		}else {
			String val = (criteria == 0) ? "Age" + ":"+splitValue : "Salary"+":"+splitValue;
			return val;
		}
	}
}

public class KDTree {
	InteriorNode root;
	
	void buildTree(int[][] points) {
		root = buildTreeUtil(points, 0, points.length-1, 1);
	}
	
	
	void insert(int[] record) {
		/* Traverse to leaf node , if full, split the node, else add it to node*/
		if(root == null) {
			root = new InteriorNode(1, record[1]);
			root.left = new InteriorNode();
			root.right = new InteriorNode();
		}
		
		InteriorNode current = root;
		InteriorNode prev = null;
		int criteria = 1;
		int path = -1;
		while(!current.isLeaf) {
			if(record[criteria]>=current.splitValue) {
				prev = current;
				current = current.right;
				path = 1;
			}else {
				prev = current;
				current = current.left;
				path = 0;
			}
			criteria = 1-criteria;
		}
		
		if(current.index < 2) {
			current.insertRecord(record);
		}
		else {
			// split the leaf node
			
			int[][] relPoints = new int[3][2];
			relPoints[0] = current.block[0];
			relPoints[1] = current.block[1];
			relPoints[2] = record;
			int prevCriteria = prev.criteria;
			Arrays.sort(relPoints,(a,b)->a[1-prevCriteria]-b[1-prevCriteria]);
			
			int index = splitPoint(0, 2, 1-prevCriteria, relPoints);
			
			
			// Splitting doesn't work
			if(index == 0 || index > 2) {
				int splitValue = -1;
				if(index>2)
					splitValue = relPoints[index-1][criteria]+1;
				
				InteriorNode proxy = new InteriorNode(1-prevCriteria, splitValue);
				InteriorNode leftNode = new InteriorNode();
				
				if(index == 0) {
					proxy.left = leftNode;
					proxy.right = current;
				}
				else if(index >2) {
					proxy.left = current;
					proxy.right = leftNode;
				}
					
				if(path == 1) {
					prev.right = proxy;
				}
				else if(path == 0) {
					prev.left = proxy;
				}
				
				prev = proxy;
				
				int pCrit = prev.criteria;
				Arrays.sort(relPoints,(a,b)->a[1-pCrit]-b[1-pCrit]);
				index = splitPoint(0, 2, 1-pCrit, relPoints);
				
				
				if(index == 0 || index > 2) {
					System.out.println("No more space. Insertion aborted");
				}
				else {
					splitValue = relPoints[index][1-pCrit];
					InteriorNode add = new InteriorNode(1-pCrit, splitValue);
					InteriorNode leftBlock = new InteriorNode();
					for(int i=0;i<index;i++) {
						leftBlock.insertRecord(relPoints[i]);
					}
					InteriorNode rightBlock = new InteriorNode();
					for(int i=index;i<3;i++) {
						rightBlock.insertRecord(relPoints[i]);
					}
					
					add.left = leftBlock;
					add.right = rightBlock;
					if(prev.left.index != 0)
						prev.left = add;
					else
						prev.right = add;

				}
			}else {
				int splitValue = relPoints[index][criteria];
				InteriorNode add = new InteriorNode(1-prev.criteria, splitValue);
				InteriorNode leftBlock = new InteriorNode();
				for(int i=0;i<index;i++) {
					leftBlock.insertRecord(relPoints[i]);
				}
				InteriorNode rightBlock = new InteriorNode();
				for(int i=index;i<3;i++) {
					rightBlock.insertRecord(relPoints[i]);
				}
				
				add.left = leftBlock;
				add.right = rightBlock;
				
				if(path == 1) {
					prev.right = add;
				}
				else if(path == 0) {
					prev.left = add;
				}
			}
			
		}
	}
	
	
	void delete(int[] record) {
		InteriorNode current = root;
		int criteria = 1;
		while(!current.isLeaf) {
			if(record[criteria]>=current.splitValue) {
				
				current = current.right;
				
			}else {
				
				current = current.left;
				
			}
			criteria = 1-criteria;
		}
		
		// Search the block
		int toDelete = -1;
		for(int i=0;i<2;i++) {
			int[] rec = current.block[i];
			if(record[0] == rec[0] && record[1] == rec[1]) {
				toDelete = i;
			}
		}
		
		if(toDelete == -1) {
			System.out.println("Record not found!");
		}
		else if(toDelete == 0) {
			current.block[0] = current.block[1];
			current.block[1] = new int[] {Integer.MIN_VALUE,Integer.MIN_VALUE};
			current.index --;
		}else if(toDelete == 1) {
			current.block[1] = new int[] {Integer.MIN_VALUE,Integer.MIN_VALUE};
			current.index --;
		}
	}
	
	void displayTree() {
		display(root,1);
	}
	
	void display(InteriorNode root, int level) {
		
		if(root!=null) {
			if(root.isLeaf && root.index == 0) return;
				
			for(int i=1;i<level;i++) System.out.print(" ");
			System.out.print("|--");
			System.out.println(root);
			if(root.left!=null) {
				display(root.left, level+1);
			}
			if(root.right!=null) {
				display(root.right,level+1);
			}
		}
		
	}
	
	
	InteriorNode buildTreeUtil(int[][] points, int start, int end, int criteria) {
		if(start>end) return null;
		
		// Leaf node
		
		if(start+1 == end) {
			InteriorNode node = new InteriorNode();
			int[] record = points[start];
			int[] record2 = points[start+1];
			node.insertRecord(record);
			node.insertRecord(record2);
			return node;
		}
		
		
		if(start == end) {
			InteriorNode node = new InteriorNode();
			int[] record = points[start];
			node.insertRecord(record);
			return node;	
		}
		
		// Interior Node
		if(criteria == 0) {
			Arrays.sort(points,start,end+1,(a,b)->a[0]-b[0]);
		}else {
			Arrays.sort(points,start,end+1,(a,b)->a[1]-b[1]);
		}
		
		
		int index = splitPoint(start,end,criteria,points);
		
		InteriorNode root = new InteriorNode(criteria,points[index][criteria]);
		root.left = buildTreeUtil(points, start, index-1, 1-criteria);
		root.right = buildTreeUtil(points, index, end, 1-criteria);
		
		return root;
		
	}
	
	int splitPoint(int start, int end, int criteria, int[][] points) {
		int size = end-start+1;
		int index = start+(size/2);
		
		int firstIndex = index,lastIndex = index;
		
		while(firstIndex-1>=start && points[firstIndex-1][criteria] == points[firstIndex][criteria]) {
			firstIndex--;
		}
		
		while(lastIndex+1 <= end && points[lastIndex+1][criteria] == points[lastIndex][criteria]) {
			lastIndex++;
		}
		
		int leftSize = Math.abs((firstIndex - start) - (end - firstIndex + 1));
		
		int rightSize = Math.abs((lastIndex+1 - start) - ((end - (lastIndex + 1)) + 1));
		
		if(leftSize<rightSize) {
			index = Math.max(firstIndex, 0);
		}else if(rightSize<=leftSize){
			index = lastIndex + 1;
		}	
		return index;
	}
	
}
