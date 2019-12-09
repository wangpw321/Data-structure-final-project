/**
*  RBtree.java
*@author Puwei Wang
*  Created by Puwei Wang on 2019-12-9.
*  Copyright © 2019 Puwei Wang. All rights reserved.
*
*/

package RedBlackTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;


public class RBtree {
    private Object key;
    private String value;
    private RBtree left;
    private RBtree right;
    private RBtree parent;
    public static boolean BLACK = false;
    public static boolean RED = true;
    private boolean color = BLACK;
	private static RBtree root;
	int size;

    public boolean isRedColor() {
        return color;
    }

    public RBtree getRoot(){
        return this.root;
    }
    
    public void setColor(boolean color) {
        this.color = color;
    }
    
    public int getSize(RBtree root) {
    	if(key==null)
    		return 0;
    	return setSize(root);
    }
    public static int setSize(RBtree key) {
    	if(key==null)
    		return 0;
    	return key.size=setSize(key.left)+setSize(key.right)+1;
    }

    public RBtree (){}
    public RBtree(Object key) {
        this.key = key;
    }
    
    private RBtree parentOf(RBtree node) {
        return node==null? null:node.parent;
    }
    
    
    private RBtree Min(RBtree node) {
        return node.left==null? node:Min(node.left);
    }
    
    private RBtree Successor(RBtree node) {
    	if (node.right!=null)
    		return Min(node.right);
    	RBtree y =node.parent;
    	while(y!=null&&node==y.right) {
    		node=y;
    		y=y.parent;
    	}
        return y;
    }
    
    private RBtree Max(RBtree node) {
        return node.left==null? node.right:Max(node.right);
    }
    

    public void leftRotate(RBtree x){
        RBtree y = x.right;
        x.right=y.left;
        if(y.left!=null){
            y.left.parent=x;
        }
        y.parent=x.parent;
        if(x.parent == null){
            RBtree.root = y;
        }else {
            if (x.parent.left==x)
                x.parent.left=y;
            else
                x.parent.right=y;
        }
        y.left=x;
        x.parent=y;
    }


    public void rightRotate(RBtree x){
        RBtree y = x.left;
        x.left=y.right;
        if(y.right!=null){
            y.right.parent=x;
        }
        y.parent=x.parent;
        if(x.parent == null){
            RBtree.root = y;
        }else {
            if (x.parent.right==x)
                x.parent.right=y;
            else
                x.parent.left=y;
        }
        y.right=x;
        x.parent=y;
    }

public void insert(Object key, String value){   
    RBtree node = new RBtree(key);   //'node' is inserting variable
    RBtree x = this.root;
    RBtree y = null;
    Comparable targetNode =null;
    while(x!=null){
        y = x;
        targetNode = (Comparable) x.key;
        if(targetNode.compareTo(key)==1)
            x = x.left;
        else 
            x = x.right;
    }
    
    node.parent=y;
    if(y != null){
        Comparable ykey = (Comparable) y.key;
        if(ykey.compareTo(key)==1)
        	y.left=node;
        else 
        	 y.right=node;
    }else 
        this.root = node;
        node.value=value;
        node.color=RED;
    insertFixUp(node);
}

private void insertFixUp(RBtree node) {
    RBtree parent,gparent;
    parent = parentOf(node);
    while(parent!=null&&parent.isRedColor()){
        gparent = parentOf(parent);
        if(parent == gparent.left){ //for cases 1 - 3
            RBtree uncle = gparent.right;
            if((uncle!=null)&&uncle.isRedColor()){  //Case 1: node’s parent is the left child of node’s 
                uncle.setColor(RBtree.BLACK);       //grandfather, and z’s uncle y is red.
                parent.setColor(RBtree.BLACK);
                gparent.setColor(RBtree.RED);
                node = gparent;
                parent = parentOf(node);
                continue;
            }
            if (parent.right == node){    
                RBtree tmp;                    //Case 2:node’s parent is the left child of node’s grandfather,
                leftRotate(parent);             // and node’s uncle y is black and z is a right child.
                tmp = parent;
                parent = node;
                node = tmp;
            }
            parent.setColor(RBtree.BLACK);    //Case 3: z’s parent is the left child of z’s grandfather
            gparent.setColor(RBtree.RED);     //, and z’s uncle y is black and z is a left child.
            rightRotate(gparent);
            parent = parentOf(node);
        }else{  
            RBtree uncle = gparent.left;
            if((uncle!=null)&&uncle.isRedColor()){  //Case 4 - 6: symmetric version
                gparent.setColor(RBtree.RED);
                uncle.setColor(RBtree.BLACK);
                parent.setColor(RBtree.BLACK);
                node = gparent;
                parent = parentOf(node) ;
                continue ;
            }
            if (parent.left == node){    
                RBtree temp;
                rightRotate(parent);
                temp = parent;
                parent = node;
                node = temp;
            }
            parent.setColor(RBtree.BLACK);    
            gparent.setColor(RBtree.RED);
            leftRotate(gparent);
            parent = parentOf(node);
        }
    }
    this.root.setColor(RBtree.BLACK);
}


public void delete(Object key){
    RBtree x = this.root;
    RBtree node = null;
    while(x!=null){           
        Comparable targetNode = (Comparable) x.key;      //Looking for the target node
        if(targetNode.compareTo(key)==1)
            x=x.left;
        else if(targetNode.compareTo(key)==-1)
            x=x.right;
        else{
            node = x;
            break;
        }

    }
    if (node == null){
        System.out.println("Not exist");
        return;
    }

    RBtree child, parent;
    boolean color;
    if(node.left!=null&&node.right!=null){
        RBtree replace = node;
        replace = Successor(replace);// looking for successor of node.
        if (node.parent!=null){     
            if(node.parent.left==node)
            	node.parent.left=replace;
            else
            	node.parent.right=replace;
        }else
            this.root = replace;

        child = replace.right;          
        parent = replace.parent;          
        color = replace.isRedColor();        
        if(parent==node)     
            parent=replace;
        else {
            if (child!=null)       
                child.parent=parent;
            parent.left=child;
            replace.right=node.right;  
            node.right.parent=replace;
        }

        replace.parent=node.parent;   
        replace.setColor(node.isRedColor());
        replace.left=node.left;
        node.left.parent=replace;   
        if(color == RBtree.BLACK)           
            deleteFixUp(child,parent);
        node = null;
        return;
    }
    if (node.left!=null)       
        child = node.left;
    else
        child = node.right;
    parent = node.parent;
    color = node.isRedColor();
    if(child!=null)                
        child.parent=parent;

    if (parent!=null){
        if (parent.left == node)
            parent.left=child;
        else
            parent.right=child;
    }else
        this.root = child;

    if (color == RBtree.BLACK)         
        deleteFixUp(child,parent);
    node = null;
}

private void deleteFixUp(RBtree child, RBtree parent) {
    RBtree brother;  //child's brother
    while((child==null||!child.isRedColor())&&(child!=this.root)){
        if (parent.left == child){     //For Cases 1 -4;
            brother = parent.right;
            if (brother!=null&&brother.isRedColor()){  // Case1 brother is red
                brother.setColor(RBtree.BLACK);
                parent.setColor(RBtree.RED);
                leftRotate(parent);
                brother  = parent.right;
            }

            if ((brother.left==null||!brother.left.isRedColor())
                &&(brother.right==null||!brother.right.isRedColor())){
                // Case2 brbrother and its both children are black
                brother.setColor(RBtree.RED);
                child = parent;
                parent = child.parent;
            }else {
                if (brother.right==null||!brother.right.isRedColor()){
                    //Case3  brbrother's black. left child in red while right child in black
                    brother.left.setColor(RBtree.BLACK);
                    brother.setColor(RBtree.RED);
                    rightRotate(brother);
                    brother = parent.right;
                }
                //Case4  brother is black, and brother.right is red

                brother.setColor(parent.isRedColor());
                parent.setColor(RBtree.BLACK);
                if (brother.right!=null)
                    brother.right.setColor(RBtree.BLACK);
                leftRotate(parent);
                child = this.root;
                break;
            }
        }else {   //symmetric version
            brother = parent.left;
            if (brother!=null&&brother.isRedColor()){  
                brother.setColor(RBtree.BLACK);
                parent.setColor(RBtree.RED);
                rightRotate(parent);
                brother = parent.left;
            }
            if ((brother.left==null||!brother.left.isRedColor())
                &&(brother.right==null||!brother.right.isRedColor())){
                
                brother.setColor(RBtree.RED);
                child = parent;
                parent = parentOf(child);
            }else {
                if (brother.left==null||!brother.left.isRedColor()){
                    brother.right.setColor(RBtree.BLACK);
                    brother.setColor(RBtree.RED);
                    leftRotate(brother);
                    brother = parent.left;
                }

                brother.setColor(parent.isRedColor());
                parent.setColor(RBtree.BLACK);
                if (brother.left!=null)
                    brother.left.setColor(RBtree.BLACK);
                rightRotate(parent);
                child = this.root;
                break;
            }
        }
    }
    if (child!=null)
        child.setColor(RBtree.BLACK);
}

private void recursiveInorder(RBtree node) {
    if (node != null) {
        recursiveInorder(node.left);
        visit(node);
        recursiveInorder(node.right);
    }
}

public void visit(RBtree node) {
    StringBuffer buff = new StringBuffer();
    if (node != null) {
        buff.append(" " + node.key +" " +node.value + "[");
        String left = node.left != null ? node.left.key + "" : "null";
        String right = node.right != null ? node.right.key + "" : "null";
        String color = node.color == RED ? "Red" : "Black";
        buff.append(left).append(" : ").append(right).append(" - ").append(color).append("] ");
        System.out.println();
    }
    System.out.print(buff.toString());
}

public static String getRandomString(int length){
    String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    Random random=new Random();
    StringBuffer sb=new StringBuffer();
    for(int i=0;i<length;i++){
      int number=random.nextInt(62);
      sb.append(str.charAt(number));
    }
    return sb.toString();
}


public static void main(String args[]){

	RBtree rbt = new RBtree();
    long ins_start=System.currentTimeMillis();
    Random r = new Random();
    ArrayList<Integer> list = new ArrayList<Integer>();
    for (int i=0;i<100000;i++){
        list.add(r.nextInt(10001));
        rbt.insert(list.get(i),getRandomString(5));
    }
    long ins_end=System.currentTimeMillis();
    System.out.println("Running time of constructing tree:"+(ins_end-ins_start)+"ms");
  // rbt.recursiveInorder(rbt.getRoot());
    System.out.println("\nSize after inserting:" + setSize(root));
    
    long del_start=System.currentTimeMillis();
    Collections.shuffle(list);
    for (int i = 0;i<100000;i++){
        rbt.delete(list.get(i));
    }
    
    long del_end=System.currentTimeMillis();
    System.out.println("Running time of destroying tree:"+(del_end-del_start)+"ms");
    System.out.println("\nSize after deleting:" + setSize(root));
    
    rbt.insert(1, "one");
    rbt.insert(2, "two");
    rbt.insert(3, "three");
    rbt.insert(4, "four");
    rbt.insert(5, "five");
    rbt.insert(6, "six");
    rbt.insert(7, "seven");
    rbt.insert(8, "eight");
    rbt.insert(9, "nine");
    rbt.insert(10, "ten");
    rbt.insert(11, "eleven");
    rbt.insert(21, "twenty one");
    rbt.insert(31, "thirty one");
    rbt.recursiveInorder(rbt.getRoot());
    
    rbt.delete(8);
    rbt.delete(10);
    rbt.delete(5);
    rbt.delete(3);
    rbt.delete(4);
    System.out.println();
    rbt.recursiveInorder(rbt.getRoot());
	/*  
 	System.out.print("\nEnter insert key number:");
 	Scanner scan_key = new Scanner(System.in);
 	int insert_key = scan_key.nextInt();

 	System.out.print("\nEnter insert value:");
 	Scanner scan_value = new Scanner(System.in);
 	String insert_value =  scan_value.next();
 	
 	RBtree.insert(insert_key, insert_value);
     System.out.println("\nSize after inserting:" + setSize(root));
    
    
     System.out.println();
*/
}
}
