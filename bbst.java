import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/*
 * Event Counter using red Black Tree. Each event with two fields: ID and count, where count is the number of active events with the given ID. 
 * written by Srinivas Gubbala
 * UFID - 2131 7376 
 */

public class bbst {

	public static String RED = "red";
	public static String BLACK = "black";

	// Node class
	public static class Node {
		public int ID;
		public int count;
		public String color;
		public Node left, right, parent;

		Node() {
		}

		public Node(int ID, int count) {

			RedBlackTree rbTree = new RedBlackTree();

			this.ID = ID;
			this.count = count;
			this.left = rbTree.nil;
			this.right = rbTree.nil;
			this.parent = rbTree.nil;
			this.color = RED;
		}
	}

	public static class RedBlackTree {

		static Node nil = new Node();

		public RedBlackTree() {
			nil.ID = -1;
			nil.count = -1;
			nil.left = nil;
			nil.right = nil;
			nil.parent = nil;
			nil.color = BLACK;
		}

		// RedBlackTree root
		static Node root = nil;
		// RedBlackTree current pointer which points to current node position
		static Node currentPtr = root;
		// count for inrange function
		static int rangeCnt = 0;

		/*
		 * Method to obtain root
		 */
		public static Node getRootNode() {
			return root;
		}

		/*
		 * Method to obtain curent pointer
		 */
		public static Node getCurrentPtr() {
			return currentPtr;
		}

		/*
		 * Method to insert node and check whether formed tree is Red Black Tree
		 * or not
		 */
		public static void insertNode(Node node) {
			if (root.count == nil.count) {
				root = node;
				node.color = BLACK;
				node.parent = nil;
				node.left = nil;
				node.right = nil;
				currentPtr = root;
			} else {
				node.parent = currentPtr;
				node.color = RED;
				currentPtr.right = node;
				currentPtr = node;
			}
			fixRBTreeInsert(node);
		}

		/*
		 * Method to insert node and check whether formed tree is Red Black Tree
		 * or not
		 */
		public static void middleInsert(Node node) {
			Node temp = root;
			if (root.count == nil.count) {
				root = node;
				node.color = BLACK;
				node.parent = nil;
				node.left = nil;
				node.right = nil;
				currentPtr = root;
			} else {
				while (true) {
					if (node.ID < temp.ID) {
						if (temp.left.count == nil.count) {
							temp.left = node;
							node.parent = temp;
							break;
						} else {
							temp = temp.left;
						}
					} else if (node.ID >= temp.ID) {
						if (temp.right.count == nil.count) {
							temp.right = node;
							node.parent = temp;
							break;
						} else {
							temp = temp.right;
						}
					}
				}
			}
			fixRBTreeInsert(node);
		}

		/*
		 * Method to fix Red Black Tree violations during insertion of new node
		 */
		public static void fixRBTreeInsert(Node node) {

			while (node != root && node.parent.color == RED) {
				Node parentSibling = nil;

				if ((node.parent == node.parent.parent.left)) {
					if (node.parent.parent.right.count != nil.count)
						parentSibling = node.parent.parent.right;

					// If parent sibling is red
					if (parentSibling.count != nil.count && parentSibling.color == RED) {
						node.parent.color = BLACK;
						parentSibling.color = BLACK;
						node.parent.parent.color = RED;
						node = node.parent.parent;
					} else {
						if (node == node.parent.right) {
							node = node.parent;
							rotateLeft(node);
						}
						node.parent.color = BLACK;
						node.parent.parent.color = RED;
						rotateRight(node.parent.parent);
					}
				} else {
					if (node.parent.parent.left.count != nil.count)
						parentSibling = node.parent.parent.left;

					// If parent sibling is red
					if (parentSibling.count != nil.count && parentSibling.color == RED) {
						node.parent.color = BLACK;
						parentSibling.color = BLACK;
						node.parent.parent.color = RED;
						node = node.parent.parent;
					} else {
						if (node == node.parent.left) {
							node = node.parent;
							rotateRight(node);
						}
						node.parent.color = BLACK;
						node.parent.parent.color = RED;
						rotateLeft(node.parent.parent);
					}
				}
			}
			root.color = BLACK;
		}

		/*
		 * Method to perform a right rotate around node
		 */
		public static void rotateRight(Node node) {
			Node temp = node.left;

			node.left = temp.right;

			if (temp.right.count != nil.count)
				temp.right.parent = node;
			temp.parent = node.parent;

			if (node.parent.count == nil.count)
				root = temp;
			else if (node == node.parent.right)
				node.parent.right = temp;
			else
				node.parent.left = temp;

			temp.right = node;
			node.parent = temp;
		}

		/*
		 * Method to perform a left rotate around node
		 */
		public static void rotateLeft(Node node) {
			Node temp = node.right;

			node.right = temp.left;

			if (temp.left.count != nil.count)
				temp.left.parent = node;
			temp.parent = node.parent;

			if (node.parent.count == nil.count)
				root = temp;
			else if (node == node.parent.left)
				node.parent.left = temp;
			else
				node.parent.right = temp;

			temp.left = node;
			node.parent = temp;
		}

		/*
		 * Method to find a node during deletion by taking id
		 */
		public static void deleteNode(int num) {
			delete(findNode(num));
		}

		/*
		 * Method to delete a node and call fix for RB Tree violations during
		 * delete
		 */
		public static void delete(Node z) {
			Node y = z;
			Node x = nil;

			String originalColor = y.color.toString();

			if (z.left.count == nil.count) {
				x = z.right;
				transplant(z, z.right);
			} else if (z.right.count == nil.count) {
				x = z.left;
				transplant(z, z.left);
			} else {
				y = treeMinimum(z.right);
				originalColor = y.color.toString();
				x = y.right;

				if (y.parent == z)
					x.parent = y;
				else {
					transplant(y, y.right);
					y.right = z.right;
					y.right.parent = y;
				}
				transplant(z, y);
				y.left = z.left;
				y.left.parent = y;
				y.color = z.color;
			}
			if (originalColor.equalsIgnoreCase(BLACK.toString()))
				fixRBTreeDelete(x);
		}

		/*
		 * Method to transplant
		 */
		public static void transplant(Node u, Node v) {
			if (u.parent.count == nil.count)
				root = v;
			else if (u == u.parent.left)
				u.parent.left = v;
			else
				u.parent.right = v;

			v.parent = u.parent;
		}

		/*
		 * Method to find the minimum node in the tree
		 */
		public static Node treeMinimum(Node node) {
			if ((node.left.count == nil.count) && (node.right.count == nil.count))
				return node;
			if (node.left.count != nil.count)
				return treeMinimum(node.left);
			if (node.right.count != nil.count)
				return treeMinimum(node.right);
			return null;
		}

		/*
		 * Method to dfix the violations during RB tree deletion
		 */
		public static void fixRBTreeDelete(Node x) {
			while (x != root && x.color == BLACK) {
				Node w = nil;
				if (x == x.parent.left) {
					w = x.parent.right;
					if (w.color == RED) {
						w.color = BLACK;
						x.parent.color = RED;
						rotateLeft(x.parent);
						w = x.parent.right;
					}
					if (w.left.color == BLACK && w.right.color == BLACK) {
						w.color = RED;
						x = x.parent;
					} else if (w.right.color == BLACK) {
						w.left.color = BLACK;
						w.color = RED;
						rotateRight(w);
						w = x.parent.right;
					}
					w.color = x.parent.color;
					x.parent.color = BLACK;
					w.right.color = BLACK;
					rotateLeft(x.parent);
					x = root;
				} else {
					w = x.parent.left;
					if (w.color == RED) {
						w.color = BLACK;
						x.parent.color = RED;
						rotateRight(x.parent);
						w = x.parent.left;
					}
					if (w.left.color == BLACK && w.right.color == BLACK) {
						w.color = RED;
						x = x.parent;
					} else if (w.left.color == BLACK) {
						w.right.color = BLACK;
						w.color = RED;
						rotateLeft(w);
						w = x.parent.left;
					}
					w.color = x.parent.color;
					x.parent.color = BLACK;
					w.left.color = BLACK;
					rotateRight(x.parent);
					x = root;
				}
			}
			x.color = BLACK;
		}

		/*
		 * Method to find the node for the given id
		 */
		public static Node findNode(int num) {
			Node current = root;
			while (current.count != nil.count) {
				if (current.ID > num)
					current = current.left;
				else if (current.ID < num)
					current = current.right;
				else
					return current;
			}
			return nil;
		}

		/*
		 * Method to print the RB tree in Inorder fashion
		 */
		public static void printInorder(Node printNode) {

			if (printNode.count == nil.count)
				return;

			printInorder(printNode.left);

			System.out.print("ID: " + printNode.ID + " -- " + ((printNode.color == RED) ? "Red" : "Black")
					+ " -- Count: " + printNode.count);

			if (printNode.parent.count != nil.count)
				System.out.print(" -- Parent: " + printNode.parent.ID);
			else
				System.out.print(" -- " + null);

			if (printNode.left.count != nil.count)
				System.out.print(" -- Left: " + printNode.left.ID);
			else
				System.out.print(" -- Left:" + null);

			if (printNode.right.count != nil.count)
				System.out.print(" -- Right:" + printNode.right.ID);
			else
				System.out.print(" -- Right:" + null);

			System.out.println("");

			printInorder(printNode.right);

		}

		/*
		 * Increase the count of the event theID by m. Insert the node, if theID
		 * is not present Print the count of theID Time complexity: O(log n).
		 */
		public static int increase(int id, int cnt) {
			Node n = RedBlackTree.findNode(id);
			if (n.count != nil.count) {
				n.count = n.count + cnt;
			} else {
				n = new Node(id, cnt);
				RedBlackTree.insertNode(n);
			}
			return n.count;
		}

		/*
		 * Reduce the count of the event theID by m. Delete the node, if count
		 * becomes < 0 Print the count of theID Time complexity: O(log n).
		 */
		public static int reduce(int id, int cnt) {
			Node n = RedBlackTree.findNode(id);
			if (n.count != nil.count)
				n.count = n.count - cnt;
			else
				n.count = 0;
			if (n.count <= 0) {
				RedBlackTree.delete(n);
				return 0;
			}
			return n.count;
		}

		/*
		 * Get the count of the event with theID by m. Print the count of theID
		 * Time complexity: O(log n).
		 */
		public static int count(int id) {
			Node n = RedBlackTree.findNode(id);
			if (n.count != nil.count)
				return n.count;
			else
				return 0;
		}

		/*
		 * Method to find the total count of events present between the given
		 * two ids including the ids
		 */
		public static int inrange(int id1, int id2) {
			rangeCnt = 0;
			return (inrangeHelper(RedBlackTree.getRootNode(), id1, id2));
		}

		/*
		 * Helper method to call recursively for In range count
		 */
		public static int inrangeHelper(Node node, int id1, int id2) {

			if (node.count == nil.count)
				return 0;

			if (id1 < node.ID)
				inrangeHelper(node.left, id1, id2);

			if (id1 <= node.ID && id2 >= node.ID)
				rangeCnt = rangeCnt + node.count;

			if (id2 > node.ID)
				inrangeHelper(node.right, id1, id2);

			return rangeCnt;
		}

		/*
		 * Print the event ID and the count of event with the lowest ID that is
		 * greater that theID. Print “0 0”, if there is no next ID. Time
		 * complexity: O(log n).
		 */
		public static String next(int id) {
			Node n = RedBlackTree.findNode(id);
			String returnStr = "";
			if (n.count == nil.count) {
				Node n1 = new Node(id, 1);
				RedBlackTree.middleInsert(n1);
				Node n2 = RedBlackTree.findNode(id);
				n = nextHelper(n2);
				if (n.count == nil.count)
					returnStr = "0 0";
				else
					returnStr = n.ID + " " + n.count;
				RedBlackTree.deleteNode(n2.ID);
			} else {
				n = nextHelper(n);
				if (n.count == nil.count)
					returnStr = "0 0";
				else
					returnStr = n.ID + " " + n.count;
			}
			return returnStr;
		}

		/*
		 * Helper method to find the next event for a given id
		 */
		public static Node nextHelper(Node n) {
			// If no left node is present
			if (n.right.count != nil.count) {
				n = n.right;
				while (n.left.count != nil.count)
					n = n.left;
				return n;
			}
			// moce to parent until the node is left to its parent
			while (n.parent.left != n && n.parent.count != nil.count)
				n = n.parent;
			return n.parent;
		}

		/*
		 * Print the event ID and the count of event with the greatest ID that
		 * is less that theID. Print “0 0”, if there is no previous ID. Time
		 * complexity: O(log n).
		 */
		public static String previous(int id) {
			Node n = RedBlackTree.findNode(id);
			String returnStr = "";

			if (n.count == nil.count) {
				Node n1 = new Node(id, 1);
				RedBlackTree.middleInsert(n1);
				n = previousHelper(n1);
				if (n.count == nil.count)
					returnStr = "0 0";
				else
					returnStr = n.ID + " " + n.count;
				RedBlackTree.deleteNode(n1.ID);
			} else {
				n = previousHelper(n);
				if (n.count == nil.count)
					returnStr = "0 0";
				else
					returnStr = n.ID + " " + n.count;
			}
			return returnStr;
		}

		/*
		 * Helper method for finding the previous event for a given id
		 */
		public static Node previousHelper(Node n) {
			// If no left node exists
			if (n.left.count != nil.count) {
				n = n.left;
				while (n.right.count != nil.count)
					n = n.right;
				return n;
			}
			// move to parent until the node is right to its parent
			while (n.parent.right != n && n.parent.count != nil.count)
				n = n.parent;
			return n.parent;
		}
	}

	// Main class
	public static void main(String[] args) throws IOException {

		long startTime = System.currentTimeMillis();

		if (args.length > 0) {

			String fileName = args[0];

			File inputNodes = new File(fileName);
			FileReader fileReader = new FileReader(inputNodes);
			BufferedReader bReader = new BufferedReader(fileReader);

			String s = bReader.readLine();

			// Count of number of events.
			int nodesCount = Integer.parseInt(s);

			s = bReader.readLine();

			RedBlackTree rbTree = new RedBlackTree();

			// Read each Event ID and its count from input file line by line
			for (int i = 0; i < nodesCount; i++) {
				String[] str = s.split(" ");
				Node n = new Node(Integer.parseInt(str[0]), Integer.parseInt(str[1]));
				// Insert nodes into the Red Black Tree
				rbTree.insertNode(n);
				s = bReader.readLine();
			}

			// Read the command-line input for the operations
			// call the corresponding methods implemented in Red Black Tree
			Scanner scanner = new Scanner(System.in);
			String op = scanner.nextLine();

			while (!"quit".equals(op)) {
				String[] opArray = op.split(" ");
				String oper = opArray[0];

				switch (oper) {

				case "increase":
					System.out.println(rbTree.increase(Integer.parseInt(opArray[1]), Integer.parseInt(opArray[2])));
					break;
				case "reduce":
					System.out.println(rbTree.reduce(Integer.parseInt(opArray[1]), Integer.parseInt(opArray[2])));
					break;
				case "count":
					System.out.println(rbTree.count(Integer.parseInt(opArray[1])));
					break;
				case "inrange":
					System.out.println(rbTree.inrange(Integer.parseInt(opArray[1]), Integer.parseInt(opArray[2])));
					break;
				case "next":
					System.out.println(rbTree.next(Integer.parseInt(opArray[1])));
					break;
				case "previous":
					System.out.println(rbTree.previous(Integer.parseInt(opArray[1])));
					break;
				default:
					System.out.println("Invalid Command");
				}
				op = scanner.nextLine();
			}
			// Print the Red Black tree in In-Order
			// rbTree.printInorder(rbTree.getRootNode());
		} else {
			System.out.println("\n Enter an input file name. \n");
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		// System.out.println("Time Taken:" + totalTime);
	}
}