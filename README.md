# ZEIT3120 Project 2 - Glitch Tracking – User Manual
## Group Members:
z5374496 – Ben Cook  
z5364429 – Don Le  
z5311627 – Kade Price  
## Contents
1. Introduction...............................................................................................................................   
1.1 Background..............................................................................................................................   
1.2 Intended Use .........................................................................................................................   
2. Instructions................................................................................................................................   
2.1 Login Window..........................................................................................................................   
2.1.1 Sign into the Application ...................................................................................................   
2.1.2 Exiting the Application ......................................................................................................   
2.1.3 Solutions...........................................................................................................................   
2.2 Inventory Window .................................................................................................................   
2.2.1 Populating the Table ......................................................................................................   
2.2.2 Navigating the Table .........................................................................................................   
2.2.3 Querying the Table............................................................................................................   
2.2.4 Navigating to the Scanning Window..................................................................................   
2.2.5 Exit Inventory....................................................................................................................   
2.2.6 Solutions...........................................................................................................................   
2.3 Scanning Window ..................................................................................................................   
2.3.1 Scan Item In ......................................................................................................................   
2.3.3 Scan Item Out ...................................................................................................................   
2.3.4 Exit Scanner Window ......................................................................................................   
2.3.5 Solutions.........................................................................................................................   
# 1. Introduction
## 1.1 Background
Glitch Tracking is a new startup aiming to offer a one-size-fits-all, highly modular and versatile
solution for inventory tracking. The company operates under a software as a service model and aims
to provide ongoing support to its clients.
## 1.2 Intended Use
The Inventory Tracking Application (ITA) is intended to be used for the purpose of tracking inventory
through the use of RFID technology. The application provides user authentication by allowing users
to sign into the system. Once signed in, users will then be able to view and search through inventory
information, which includes items, stores, quantities, summaries, and suppliers. In addition to this,
users will be able to scan items in and out of the database using RFID technology to keep track of the
stock levels in each store.
# 2. Instructions
## 2.1 Login Window
### 2.1.1 Sign into the Application
The login window is the first window the user will see when launching the application. Here they will
be prompted to enter their staff id and associated password.
They can do this by entering their assigned staff ID in the top field followed by their assigned
password in the bottom field. Once completed users can either hit enter or the sign in button to
progress to the Inventory window.
### 2.1.2 Exiting the Application
To exit the application users can click on the red X that is present in top right corner.
### 2.1.3 Solutions
1. If the user is unable to login with the inputted credentials it is important to ensure that
they are using a valid staff ID.
2. If the staff ID is valid and issues continue to persist, make sure that the inputted
password conforms with our standards to see if a mistake has occurred somewhere.
a. All passwords within our system must be a minimum of 12 characters long.
b. All passwords must consist of at least one of each of the following:
i. Upper-case letter
ii. Lower-case letter
iii. Number
iv. Special character
3. If any issues persist feel free to contact the Glitch Tracking team and we will be happy to
help sort out whatever your problem may be.
## 2.2 Inventory Window
### 2.2.1 Populating the Table
To populate the table, all users need to do is press the large search button on the right side of the
screen. Doing so will cause the table to load all information that is contained within the database.
### 2.2.2 Navigating the Table
To navigate the table users are able to use the scroll bar on the right side of the table to view the
information that does not fit on the page.
### 2.2.3 Querying the Table
To query the table, users have the option of 3 fields and 1 checkbox to tailor the query in a way that
suits them.
For example, a user can search for apples in the table by typing the word apple in the item name
field and then hitting either enter or the search button on the right.
This query can then be further tailored to the user by narrowing the store the apple is located at. In
this case a user can type the word GST in the store name field and then hit either enter or the search
button.
In addition to this the supplier can also be specified when conducting a query. In this case typing in
BCD in the supplier name field and then hitting either enter or search will once again narrow the
results even further.
Finally, the user can check to see whether the item has a low stock by checking the “show only low
stock” box and then clicking the search button. In this case there are no entries because the apple
fitting this criteria has more than enough already in stock.
However, if the other queries are removed and the search is then refreshed by hitting the search
button, it is possible to see all the items that have a low quantity.
### 2.2.4 Navigating to the Scanning Window
To navigate to the Scanning Window user can click on the Scan In/Out button in the top left corner of
the screen.
### 2.2.5 Exit Inventory
To return to the Login Window and exit the inventory users can click on the Sign Out button in the
top right corner of the screen.
### 2.2.6 Solutions
1. If the table is blank make sure that all the fields are empty, and then click the search
button.
2. If query returns no results make sure that whatever is being searched for is spelt
correctly.
3. If issues persist feel free to contact the Glitch Tracking team and we will be happy to help
sort out whatever your problem may be.
## 2.3 Scanning Window
### 2.3.1 Scan Item In
To scan an item into the database first enter the id of the item you want to add in the item ID field.
This should auto populate the RFID field with the corresponding RFID value.
Then enter the name of the store you want to add the item to.
Finally enter the quantity of the item to add.
Once all fields are populated click the scan in button.
A confirmation message will then appear to confirm that the process was successfully completed.
### 2.3.3 Scan Item Out
To scan an item out of the database first enter the id of the item you want to remove in the item ID
field.
This should auto populate the RFID field with the corresponding RFID value.
Then enter the name of the store you want to remove the item from.
Finally enter the quantity of the item to remove.
Once all fields are populated click the scan out button.
A confirmation message will then appear to confirm that the process was successfully completed.
### 2.3.4 Exit Scanner Window
To return to the Inventory Window press the back button in the bottom left corner.
### 2.3.5 Solutions
1. If the “please fill in all fields” error message is encountered ensure that all fields contain
information.
2. If the “please enter a valid quantity” error message is encountered ensure that the
quantity field only contains digits that are positive e.g., “10” and not “-10” or “0”.
3. If the “item not scanned in successfully” error message is encountered ensure that a
valid item id is entered and ensure that a valid store name is entered.
4. If issues persist feel free to contact the Glitch Tracking team and we will be happy to help
sort out whatever your problem may be
