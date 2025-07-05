# Fawary Internship Assessment Project

This is a simple eCommerce website demonstrating the use of the core OOP concepts using Java. The project is build with interactive console interface, allowing users to communicate with the system and perform various actions such as adding products to the cart, placing orders, tracking orders, etc. 

# Table of Contents
1. [Models](#models)
2. [Design Choices](#design-choices)
4. [Examples and Results](#examples-and-results)
3. [Installation and Usage](#installation-and-usage)

# Models
The project consists of the following models:
- `Product`: Represents a product in the eCommerce system. it's a generic class that can be exteneded to create specific products such as `Electronics`, `Clothing`, etc.
- `Cart`: Represents a shopping cart that holds products added by the user.
- `Order`: Represents an order placed by the user, containing a list of products and the total price, including shipping costs and a real-time status of the order.
- `Customer`: Represents a customer in the eCommerce system, containing personal information, balance, shopping cart and a list of orders placed by the customer.
- `ShippingService`: Stimulates the shipping service role in the eCommerce system, providing methods to calculate shipping costs, track orders and changing orders' status accordingly.
- `Inventory`: Represents the inventory of the eCommerce system, managing the products available for sale and their quantities. Build using Trie data structure to allow fast prefix searrching and easy retrieval of products.

There are classes that implements different interfaces to provide additional functionalities, such as `Shippable` for products that can be shipped and `Expirable` for products that have an expiration date.

There are also some helper classes such as `CartItem`, `OrderStatus`, `NoEnoughProducts`, `ProductNotFound`, etc.


# Design Choices

This project is designed to demonstrate the core OOP concepts such as encapsulation, inheritance, polymorphism and abstraction. Also, I decided to advance by making it an interactive system that anyone can use, using advanced Trie data structure for adding additional functionalities than just a simple eCommerce system.

# Examples and Results
Here are some examples of how to use the project and how system is responding to some actions and corner cases:

## Examples
### Showing the inventory items (Code Snippet)
```java
Map<Product, Integer> allProducts = inventory.getAllProducts();
```
### Showing the inventory items (Toplevel)
```plain
>>> show inventory
Number of different products: 30
-----------------------------------------
1X Brie	(CHE206)	48.0
4X Samsung Smart TV	(TV103)	4599.99
3X The Great Gatsby	(BOK105)	78.9
7X WD External HDD 1TB	(HDP701)	1399.99
4X Ricotta	(CHE208)	43.0
....
3X Swiss	(CHE212)	41.0
6X Cottage	(CHE214)	38.0
3X MacBook Air M1	(LPT302)	38999.99
2X Canon DSLR 250D	(CAM501)	19999.99
-------------------------------------
Want to buy anything ? use the 'add' command to add to the cart
```

### Adding a product to the cart (Code Snippet)
```java
user.cart.addItem(productID, quantity, inventory);
```
### Adding a product to the cart (Toplevel)
```plain
>>> show cart
Cart is empty right now.
>>> add LPT302 1
>>> show cart
** Chart Items **

Number of items: 1
------------------
1X MacBook Air M1(LPT302)	38999.99
------------------
Total Price : 38999.99
Shipping Fees : 1800.0
```

### Removing a product from the cart (Code Snippet)
```java
user.cart.removeItem(productID, quantity, inventory);
```
### Removing a product from the cart (Toplevel)
```plain
>>> remove LPT302 1
>>> show cart
Cart is empty right now.
```

### Manipulating the balance
```plain
>>> show balance
1000.0
>>> balance 100000
>>> show balance
100000.0
```

### Lookup in the inventory using prefix search
```plain
>>> show inventory c
Number of different products: 5
-----------------------------------------
8X Camembert	(CHE207)	44.0
5X Cheddar	(CHE202)	60.0
7X Cream Cheese	(CHE209)	47.0
6X Cottage	(CHE214)	38.0
2X Canon DSLR 250D	(CAM501)	19999.99
-------------------------------------
Want to buy anything ? use the 'add' command to add to the cart

>>> show inventory ca
Number of different products: 2
-----------------------------------------
8X Camembert	(CHE207)	44.0
2X Canon DSLR 250D	(CAM501)	19999.99
-------------------------------------
Want to buy anything ? use the 'add' command to add to the cart
```

## Results

This section illustrates the results of checkout for different scenarios, including successful checkout, insufficient balance, expired products, insufficient stock, and more.

### Successful Checkout
```plain
>>> add LPT301 1
>>> add WTC601 1
>>> show cart
** Chart Items **

Number of items: 2
------------------
1X Apple Watch SE(WTC601)	10999.99
1X Dell XPS 13(LPT301)	34999.99
------------------
Total Price : 45999.977
Shipping Fees : 2150.0
>>> balance 100000

>>> checkout
** ORDER #ORD0 **
STATUS: PROCESSING
OrderedAt: 2025-07-05

** Shipment Notice **
1X Apple Watch SE	10000.0
1X Dell XPS 13	33000.0
----------------
Total Package Weight: 43000.0

** Order Items **
1X Apple Watch SE	10999.99
1X Dell XPS 13	34999.99
---------------
Subtotal Price: 45999.977
Shipping Fees: 2150.0

Order Price: 48149.977



Transaction done successfully
```

This shows a successful checkout where the user only orders shippable products, but what about ordering non-shippable products ?

### Shippable and non-shippable products transaction
I choose to place the shippable products in separate order from the non-shippable products, so the user can track them separately and also to avoid any issues with shipping costs and tracking.

```plain
>>> add CHE209 2
>>> add TV102 1
>>> add BOK103 1
>>> show cart
** Chart Items **

Number of items: 3
------------------
2X Cream Cheese(CHE209)	94.0
1X LG TV Full HD(TV102)	3999.99
1X 1984(BOK103)	85.0
------------------
Total Price : 4178.99
Shipping Fees : 175.025
>>> checkout
** ORDER #ORD0 **
STATUS: PROCESSING
OrderedAt: 2025-07-05

** Shipment Notice **
1X LG TV Full HD	3500.5
----------------
Total Package Weight: 3500.5

** Order Items **
1X LG TV Full HD	3999.99
---------------
Subtotal Price: 3999.99
Shipping Fees: 175.025

Order Price: 4175.015


** ORDER #ORD1 **
STATUS: PROCESSING
OrderedAt: 2025-07-05

** Order Items **
2X Cream Cheese	94.0
1X 1984	85.0
---------------
Subtotal Price: 179.0

Order Price: 179.0



Transaction done successfully
```

Here we can see that the user ordered a shippable product (LG TV Full HD) and two non-shippable products (Cream Cheese and 1984). The system created two separate orders, one for the shippable product and another for the non-shippable products.

Shippable orders have 3 statuses: `PROCESSING`, `SHIPPED` and `RECEIVED`, while non-shippable orders have only 2 statuses: `PROCESSING` and `RECEIVED`. What will happen if we tries to ship a non-shippable product ? 

### Non-shippable product shipping attempt
```plain
>>> ship ORD1

Order Not Shippable
```
### Order Shipping for Shippable Products
```plain
>>> ship ORD0
>>> show orders ORD0
** ORDER #ORD0 **
STATUS: SHIPPED
OrderedAt: 2025-07-05

** Shipment Notice **
1X LG TV Full HD	3500.5
----------------
Total Package Weight: 3500.5

** Order Items **
1X LG TV Full HD	3999.99
---------------
Subtotal Price: 3999.99
Shipping Fees: 175.025

Order Price: 4175.015
```

### Order Tracking
```plain
>>> show orders ORD0
** ORDER #ORD0 **
STATUS: SHIPPED
OrderedAt: 2025-07-05

** Shipment Notice **
1X LG TV Full HD	3500.5
----------------
Total Package Weight: 3500.5

** Order Items **
1X LG TV Full HD	3999.99
---------------
Subtotal Price: 3999.99
Shipping Fees: 175.025

Order Price: 4175.015


>>> confirm ORD0
>>> show orders ORD0
** ORDER #ORD0 **
STATUS: RECEIVED
OrderedAt: 2025-07-05

** Shipment Notice **
1X LG TV Full HD	3500.5
----------------
Total Package Weight: 3500.5

** Order Items **
1X LG TV Full HD	3999.99
---------------
Subtotal Price: 3999.99
Shipping Fees: 175.025

Order Price: 4175.015
```

Now, we can see more about corner cases and how the system handles them.

### Insufficient Balance
```plain
>>> add LPT301 1
>>> show cart
** Chart Items **

Number of items: 1
------------------
1X Dell XPS 13(LPT301)	34999.99
------------------
Total Price : 34999.99
Shipping Fees : 1650.0
>>> show balance
1000.0
>>> checkout
Your balance is insufficient, you got 1000.0 and the order is worth 36649.99
```

### Expired Products
```java
Dairy prod2  = new Dairy("CHE202", "Cheddar",      50f, 60f, LocalDate.of(2023, 12, 1));  // expired
addToInventory(prod2, 5);
```
```plain
>>> add CHE202
>>> show cart
** Chart Items **

Number of items: 1
------------------
1X Cheddar(CHE202)	60.0
------------------
Total Price : 60.0
WARNING: Some of the products in the cart has expired
	- Cheddar (CHE202)
Make sure to remove them by using the command 'remove [productId] [quantity]' before checkout
>>> checkout

Transaction Failed

-------------------------------
Some of the products in the cart has expired
	- Cheddar (CHE202)
Make sure to remove them by using the command 'remove [productId] [quantity]' before checkout again
```

### Insufficient Stock
```java
Dairy prod4  = new Dairy("CHE204", "Mozzarella",  50f, 60f, LocalDate.of(2025, 12, 1));  // not expired
addToInventory(prod4, 10);
```
```plain
>>> add CHE204 15
>>> show cart
** Chart Items **

Number of items: 1
------------------
15X Mozzarella(CHE204)	675.0
------------------
Total Price : 675.0
>>> show balance
1000.0
>>> checkout

Transaction Failed

-------------------------------
Some of the products in the cart are either out of stock or with insufficient amount
Items out of stock are automatically removed from the cart, Here is a list of insufficient amount products (if any)
-------------------------------------------------
Product: Mozzarella(CHE204) 15 Available: 10
Make sure to remove them by using the command 'remove [productId] [quantity]' before checkout again
```

## Insufficient Stock with Expired Products
```java
Dairy prod2  = new Dairy("CHE202", "Cheddar", 50f, 60f, LocalDate.of(2023, 12, 1));  // expired
addToInventory(prod2, 5);

Electronic device5  = new Electronic("LPT301", "Dell XPS 13", 33000.00f, 34999.99f);
addToInventory(device5, 1);


```
```plain
>>> add CHE202 4
>>> add LPT301 2
>>> show cart
** Chart Items **

Number of items: 2
------------------
2X Dell XPS 13(LPT301)	69999.98
4X Cheddar(CHE202)	240.0
------------------
Total Price : 70239.98
Shipping Fees : 3300.0
WARNING: Some of the products in the cart has expired
	- Cheddar (CHE202)
Make sure to remove them by using the command 'remove [productId] [quantity]' before checkout
>>> checkout

Transaction Failed

-------------------------------
Some of the products in the cart has expired
	- Cheddar (CHE202)
Some of the products in the cart are either out of stock or with insufficient amount
Items out of stock are automatically removed from the cart, Here is a list of insufficient amount products (if any)
-------------------------------------------------
Product: Dell XPS 13(LPT301) 2 Available: 1
Make sure to remove them by using the command 'remove [productId] [quantity]' before checkout again

```

# Installation and Usage
To run the project, you need to have Java installed on your machine. You can clone the repository and run the `Main.java` file to start the interactive console interface.

```bash
git clone https://github.com/abanoub-samy-farhan/Fawary_Internship_Assessment
cd Fawary_Internship_Assessment/src
mkdir out
javac -d out ecommerce/*.java ecommerce/models/*.java ecommerce/services/*.java ecommerce/utils/*.java ecommerce/Main.java
java -cp out ecommerce.Main
```

