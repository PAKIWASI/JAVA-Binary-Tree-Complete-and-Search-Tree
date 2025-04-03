import java.util.ArrayDeque;
import java.util.Queue;


public class BinaryTree<T> 
{
    private Node<T> root;
    
    private int depth;
    private int size;
    private boolean isSearchTree;

    private static class Node<T>
    {
        private T data;
        private Node<T> left;
        private Node<T> right;

        private Node(T data)
        {
            this.data = data;
            left = null;
            right = null;
        }

        private T getData() { return data; }

        private void setData(T data) { this.data = data; }

        private Node<T> getLeft() { return left; }

        private void setLeft(Node<T> left) { this.left = left; }

        private Node<T> getRight() { return right; }

        private void setRight(Node<T> right) { this.right = right; }
    }

    public BinaryTree()
    {
        root = null;
        depth = 0;
        size = 0;
        isSearchTree = false;
    }

    public void add(T data)
    {
        Node<T> newNode = new Node<>(data);
        
        if (root == null)
            root = newNode;
         
        else {
            if (isSearchTree)
                insertBST(root, newNode); // BST insertion
            else
                insertCompleteBinaryTree(newNode); // Complete binary tree insertion
        }
        size++;
    }

    private void insertBST(Node<T> root, Node<T> newNode)
    {                       // some type convertion stuff, as we are using generics
        Comparable<? super T> comparableNewData = (Comparable<? super T>) newNode.getData();
        Node<T> current = root;
        Node<T> parent = null;
        
        while (current != null)
        {
            parent = current;  
            int cmp = comparableNewData.compareTo(current.getData()); 
            // if int, -ve if "this" is less, 0 if equal and +ve is greater 
            if (cmp < 0)
                current = current.getLeft();
            else if (cmp > 0)
                current = current.getRight();
            else
            {    
                System.out.println("Duplicate values not allowed in BST");
                return;
            }
        }
        // Attach the new node to the parent
        int cmp = comparableNewData.compareTo(parent.getData());
        if (cmp < 0)
            parent.setLeft(newNode);
        else
            parent.setRight(newNode);
    }
    
    private void insertCompleteBinaryTree(Node<T> newNode)
    {
        Queue<Node<T>> queue = new ArrayDeque<>();
        queue.add(root);
        
        while (!queue.isEmpty())
        {
            Node<T> current = queue.poll();
            
            if (current.getLeft() == null)
            {
                current.setLeft(newNode);
                return;
            } else
                queue.add(current.getLeft());
            
            if (current.getRight() == null)
            {
                current.setRight(newNode);
                return;
            } else
                queue.add(current.getRight());
        }
    }
    
    public int maxDepth()
    {
        depth = maxDepth(root);
        return depth;
    }

    private int maxDepth(Node<T> root)
    {
        if (root == null)
            return 0;
        else    
        {
            int l = 1 + maxDepth(root.getLeft());
            int r = 1 + maxDepth(root.getRight());
            return Math.max(l, r);
        }
    }

    public void setSearchTree() { isSearchTree = true; }

    public boolean isSearchTree() { return isSearchTree; }

    public boolean search(T data)
    {
        Comparable<? super T> comparableData = (Comparable<? super T>) data;
        if (isSearchTree)
            return searchBST(data, comparableData);
        else    
            return searchBFS(data, comparableData);
    }

    private boolean searchBFS(T data, Comparable<? super T> comparableData)
    {
        Queue<Node<T>> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty())
        {
            Node<T> node = q.poll();

            int cmp = comparableData.compareTo(node.getData());
            if (cmp == 0)
                return true;  // Found
            
            if (node.getLeft() != null)
                q.add(node.getLeft());
            
            if (node.getRight() != null)
                q.add(node.getRight());
        }
        return false;
    }
    
    private boolean searchBST(T data, Comparable<? super T> comparableData)
    {
        Node<T> current = root;
    
        while (current != null)
        {
            int cmp = comparableData.compareTo(current.getData());
            
            if (cmp == 0)
                return true;  // Found
            else if (cmp < 0)
                current = current.getLeft();  // Move left
            else
                current = current.getRight(); // Move right
        }
        return false;  // Not found
    }

    public static enum DFSmode
    {
        PreOrder, // node, left, right 
        InOrder,  // left, node, right
        PostOrder;// left, right, node
    }

    public StringBuilder DFSTraversal(DFSmode mode) // stack or recurtion
    {
        StringBuilder str = new StringBuilder();
        switch (mode)
        {
            case PreOrder:
                return PreOrder(root, str);
                
            case InOrder:
                return InOrder(root, str);
                
            case PostOrder:
                return PostOrder(root, str);
            default:
                return null;    
        }
    }

    private StringBuilder PreOrder(Node<T> node, StringBuilder str)
    {
        if (node != null)
        {
            str.append(node.getData()).append(" ");
            PreOrder(node.getLeft(), str);
            PreOrder(node.getRight(), str);
        }
        return str;
    }

    private StringBuilder InOrder(Node<T> node, StringBuilder str)
    {
        if (node != null)
        {
            InOrder(node.getLeft(), str);
            str.append(node.getData()).append(" ");
            InOrder(node.getRight(), str);
        }
        return str;
    }

    private StringBuilder PostOrder(Node<T> node, StringBuilder str)
    {
        if (node != null)
        {
            PostOrder(node.getLeft(), str);
            PostOrder(node.getRight(), str);
            str.append(node.getData()).append(" ");
        }
        return str;
    }

    public StringBuilder BFS()  // queue 
    {
        if (root == null)
            return null;
        StringBuilder str = new StringBuilder();
         
        return levelOrder(root, str);
    }

    private StringBuilder levelOrder(Node<T> root, StringBuilder str)
    {
       Queue<Node<T>> queue = new ArrayDeque<Node<T>>();
       queue.add(root);
       while (!queue.isEmpty())
       {
           Node<T> tempNode = queue.poll();
           str.append(tempNode.getData()).append(" ");

           if (tempNode.getLeft() != null)
               queue.add(tempNode.left);

           if (tempNode.getRight() != null)
               queue.add(tempNode.getRight());
       }
       return str;
   }

    public String toString()
    {
        printTree(root, "", true);
        return "";
    }

    private void printTree(Node<T> node, String prefix, boolean isLeft)
    {
        if (node == null)
            return;

        System.out.println(prefix + (isLeft ? "├── " : "└── ") + node.getData());
        printTree(node.getLeft(), prefix + (isLeft ? "│   " : "    "), true);
        printTree(node.getRight(), prefix + (isLeft ? "│   " : "    "), false);
    }
    
}