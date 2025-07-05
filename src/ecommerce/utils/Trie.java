package ecommerce.utils;

import ecommerce.models.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class TrieNode{
    public int quantity;
    TrieNode[] children;
    Product product;
    boolean end;

    TrieNode(){
        children = new TrieNode[26];
        end = false;
        product = null;
        quantity = 0;
    }

    public boolean isEmpty(){
        boolean empty = true;
        for (TrieNode node : children){
            if(node != null) return false;
        }
        return empty;
    }

}

public class Trie {

    private final TrieNode root;

    public Trie(){
        root = new TrieNode();
    }

    public void insert(Product p, int quantity){
        TrieNode node = root;
        String name = p.getName().toLowerCase();
        for(int i = 0; i < name.length(); i++){
            char c = name.charAt(i);
            if (c < 'a' || c > 'z') continue;
            if (node.children[c - 'a'] == null){
                node.children[c - 'a'] = new TrieNode();
            }
            node = node.children[c -'a'];
        }
        if (node.end) {
            node.quantity+=quantity;
            return;
        }
        node.end = true;
        node.product = p;
        node.quantity = quantity;
    }

    public Product search(String product) throws ProductNodeFound{
        TrieNode node = root;
        product = product.toLowerCase();
        for(int i = 0; i < product.length(); i++){
            char c = product.charAt(i);
            if (c < 'a' || c > 'z') continue;
            if (node.children[c-'a'] == null){
                throw new ProductNodeFound("No product named " + product);
            }
            node = node.children[c-'a'];
        }
        return node.product;
    }

    public int amount(Product p){
        TrieNode node = root;
        String product = p.getName().toLowerCase();
        for(int i = 0; i < product.length(); i++){
            char c = product.charAt(i);
            if (c < 'a'  || c > 'z') continue;
            if (node.children[ c - 'a'] == null){
                return 0;
            }
            node = node.children[ c - 'a'];
        }
        return node.quantity;
    }

    public Map<Product, Integer> startWith(String product) throws ProductNodeFound {
        Map<Product, Integer> products =  new HashMap<Product, Integer>();
        TrieNode node = root;
        product = product.toLowerCase();
        for(int i = 0; i < product.length(); i++){
            char c = product.charAt(i);
            if (c < 'a' || c > 'z') continue;
            if (node.children[c-'a'] == null){
                throw new ProductNodeFound("No products start with " + product);
            }
            node = node.children[c-'a'];
        }
        dfs(node, products, product);
        return products;
    }

    private void dfs(TrieNode node, Map<Product, Integer> products, String prefix){
        if (node == null) return;
        if (node.end) {
            products.put(node.product, node.quantity);
        }
        for (char c = 'a'; c <= 'z'; c++){
            dfs(node.children[c - 'a'], products, prefix + c);
        }
    }

    public void delete(Product p, int quantity) throws ProductNodeFound {
        TrieNode node = root;
        String product = p.getName().toLowerCase();

        Stack<TrieNode> parents = new Stack<TrieNode>();
        parents.push(node);
        for(int i = 0; i < product.length(); i++){
            char c = product.charAt(i);
            if (c < 'a' || c > 'z') continue;
            if (node.children[c -'a'] == null){
                throw new ProductNodeFound("No product with the name " + product);
            }
            node = node.children[c-'a'];
            parents.push(node);

        }
        if (node.end){
            if (node.quantity - quantity < 0)
                throw new NoEnoughProdcuts("Store has less than " + quantity + " of " + p.getName());
            node.quantity -= quantity;
            if (node.quantity == 0){
                node.end = false;
                node.product = null;
                if (node.isEmpty()){
                    for (int ind = product.length() - 1; ind >= 0; ind--){
                        if (product.charAt(ind) > 'a' ||  product.charAt(ind) < 'z') continue;
                        TrieNode curr =  parents.pop();
                        TrieNode parent = parents.peek();
                        if (curr.isEmpty() && !curr.end){
                            System.out.println(product.charAt(ind));
                            parent.children[product.charAt(ind) - 'a'] = null;
                        } else
                            break;
                    }
                }
            }
        } else {
            throw new  ProductNodeFound("No product with the name " + p.getName());
        }
    }
}
