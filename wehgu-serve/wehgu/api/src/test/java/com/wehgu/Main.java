package com.wehgu;

import java.util.*;

class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;
    public TreeNode(int _val) {
        val = _val;
    }
}
class Leave {
    public int val;
    public int level;
    public Leave(int _val,int _level) {
        val = _val;
        level = _level;
    }
}


public class Main {
    public static void main(String[] args) {
        Main solution = new Main();

        TreeNode root = solution.initTree();
        int result = solution.sumOfShallowestLeaves(root);
        System.out.print(result);
    }

    public ArrayList<String> calcTax (ArrayList<Double> income) {
        // write code here
        ArrayList<String> list =new ArrayList();
        HashMap<Integer,Double> map =new HashMap();

        map.put(5000,0.00);
        map.put(31000,0.03);
        map.put(108000,0.10);
        map.put(156000,0.20);
        map.put(120000,0.25);
        map.put(240000,0.30);
        map.put(300000,0.35);
//         map.put(Integer.MAX_VALUE,0.45);

        for (Double item:income) {
            double taxi=0;
            for (Map.Entry<Integer, Double> entry : map.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                if(item>entry.getKey()){
                    taxi+=entry.getKey()*entry.getValue();
                    item-=entry.getKey();
                }else{
                    taxi+=item*entry.getValue();
                    break;
                };

            };
            list.add(String.valueOf(taxi));
        };

        return list;
    };


    private int sumOfShallowestLeaves(TreeNode root) {
        // Write your code here.
        // ...
        ArrayList<Leave> arr=new ArrayList<>();
        traverseTree(root,1,arr);
        int max=-100000;
        int count=0;
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).level>max){
                count=0;
                max=arr.get(i).level;
                count+=arr.get(i).val;
            }else if(arr.get(i).level==max){
                count+=arr.get(i).val;
            }
        }
        
        return count;
        
    }
    
    public void traverseTree(TreeNode node,int level,ArrayList<Leave> arr) {
        // Write your code here.
        // ...
        Leave leave= new Leave(node.val, level);
        arr.add(leave);
        if(node.left==null&&node.right==null) return ;
            
        level++;
        
        if(node.left!=null) {
           traverseTree(node.left,level,arr);
        }else{
           traverseTree(node.right,level,arr);
        }
        
    }
 

    private TreeNode initTree() {
        Scanner scan = new Scanner(System.in);
        Queue<TreeNode> nodes = new LinkedList<TreeNode>();
        nodes.offer(new TreeNode(scan.nextInt()));
        TreeNode root = nodes.peek();
        while (!nodes.isEmpty()) {
            TreeNode node = nodes.poll();
            int lval = scan.nextInt();
            int rval = scan.nextInt();
            if (lval >= 0) {
                TreeNode lnode = new TreeNode(lval);
                node.left = lnode;
                nodes.offer(lnode);
            }
            if (rval >= 0) {
                TreeNode rnode = new TreeNode(rval);
                node.right = rnode;
                nodes.offer(rnode);
            }
        }
        return root;
    }
}