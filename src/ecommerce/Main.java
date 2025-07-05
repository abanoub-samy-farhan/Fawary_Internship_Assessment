package ecommerce;


import ecommerce.models.Customer;
import ecommerce.models.Product;
import ecommerce.services.Inventory;
import ecommerce.services.Order;
import ecommerce.utils.ProductNodeFound;

import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // making interactive system

        Scanner input = new Scanner(System.in);
        Inventory inventory = new Inventory();

        System.out.println("Welcome to the My eCommerce website");
        System.out.print("Please enter your first name: ");
        String firstName = input.nextLine().trim();
        System.out.print("Please enter your last name: ");
        String lastName = input.nextLine().trim();

        Customer user =  new Customer(firstName, lastName, "test@gmail.com", 1000F);
        System.out.println("Welcome" + firstName + " " + lastName);
        System.out.println("Type 'help' to know all the available commands. \n");
        while(true){
            System.out.print(">>> ");
            String[] arg = input.nextLine().trim().split(" ");
            String command = arg[0].toLowerCase();

            switch(command){
                case "help":
                    showHelp("");
                    break;

                    case "balance":
                        balanceCommand(arg, user);
                        break;
                case "show":
                    showCommand(arg, user, inventory);
                    break;
                    case "add":
                        addCommand(arg, user, inventory);
                        break;
                        case "remove":
                            removeCommand(arg, user);
                            break;
                case "ship":
                    shipCommand(arg, user);
                    break;
                case "confirm":
                    confirmCommand(arg, user);
                    break;
                case "checkout":
                    checkout(user, inventory);

            }
        }
    }

    private static void showHelp(String command){
        if (command == null){
            // print the default help
        } else{
            // print the command help menu
        }
    }

    private static void balanceCommand(String [] args, Customer user){
        if (args.length < 2){
            System.out.println("Please enter your amount to balance");
        } else{
            try {
                float newBalance = Float.parseFloat(args[1]);
                user.setBalance(newBalance);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount to balance");
            }
        }
    }

    private static void showCommand(String [] args,  Customer user, Inventory inventory){
        if (args.length < 2){
            System.out.println("Please specify what you want to see (cart, inventory, balance, orders)");
        }
        else {
            if (args[1].equals("cart")){
                user.cart.viewChart();
            }
            else if (args[1].equalsIgnoreCase("inventory")){
                Map<Product, Integer> allProducts;
                if (args.length == 3){
                    allProducts = inventory.searchProducts(args[2]);
                } else {
                    allProducts = inventory.getAllProducts();
                }

                if (allProducts == null){
                    return;
                }
                System.out.println("Number of different products: " + allProducts.size());
                System.out.println("-----------------------------------------");
                for (Map.Entry<Product, Integer> entry : allProducts.entrySet()){
                    System.out.println(entry.getValue() + "X " + entry.getKey().toString());
                }

                System.out.println("-------------------------------------");
                System.out.println("Want to buy anything ? use the 'add' command to add to the cart");
            }
            else if (args[1].equalsIgnoreCase("orders")){
                if (args.length == 3)
                {
                    String orderId =  args[2];
                    if (user.orders.containsKey(orderId)){
                        user.orders.get(orderId).display();
                    } else{
                        System.out.println("There is no order with that ID provided, it's case sensitive");
                    }
                }
                else {
                    user.viewOrderHistory();
                }
            }
            else if (args[1].equalsIgnoreCase("balance")){
                System.out.println(user.getBalance());
            }
        }
    }

    private static void addCommand(String [] args, Customer user, Inventory inventory) {
        if (args.length < 2){
            System.out.println("Please enter the ID of the product you want to add to the cart");
        }
        else if (args.length < 3){
            user.cart.addItem(args[1], 1, inventory);
        }
        else if (args.length == 3){
            try{
                user.cart.addItem(args[1], Integer.parseInt(args[2]), inventory);
            } catch (NumberFormatException e){
                System.out.println("Please enter a valid quantity or don't enter quantity for default 1 item addition");
            }
        }
    }

    private static void removeCommand(String [] args, Customer user){
        if (args.length < 2){
            System.out.println("Please enter the ID of the product you want to remove from the cart");
        }
        else if (args.length < 3){
            user.cart.removeItem(args[1], 1);
        }
        else if (args.length == 3){
            try {
                user.cart.removeItem(args[1], Integer.parseInt(args[2]));
            } catch (NumberFormatException e){
                System.out.println("Please enter a valid quantity or don't enter quantity for default 1 item removal");
            }
        }
    }

    private static void confirmCommand(String [] args, Customer user){
        if (args.length < 2){
            System.out.println("Please enter the order you want to confirm");
        }
        else  if (args.length < 3){
            Order order = user.orders.get(args[1]);
            if (order == null){
                System.out.println("There is no order with that ID provided, it's case sensitive");
                return;
            }
            order.ConfirmOrder();
        }
    }

    private static void shipCommand(String[] args, Customer user){
        if (args.length < 2){
            System.out.println("Please enter the order you want to ship");
        } else if  (args.length < 3){
            Order order = user.orders.get(args[1]);
            if (order == null){
                System.out.println("There is no order with that ID provided, it's case sensitive");
                return;
            }
            order.ShipOrder();
        }
    }

    private static void checkout(Customer user, Inventory inventory){
        try {
            user.cart.checkout(user, inventory);
        } catch (ProductNodeFound e) {
            System.out.println("There is no order with that ID provided, it's case sensitive");
        }
    }
}