# 📚 Online Bookstore Java Console Application

A comprehensive Java console-based application simulating an online bookstore with role-based access (Admin/User). Users can register, login, view, search, and purchase books, while Admins can manage book inventory.

## 👨‍💻 Developed By
**Zubaria Sajjad **

---

## 📌 Table of Contents
- [Features]
- [Project Structure]
- [How to Run]
- [User Flow]
- [Admin Flow]
- [File Storage Format]
- [Technology Stack]
- [Validation]
- [Contributing]
- [License]

---

## 🚀 Features

### 🧑‍💼 Admin
- Admin Login using email/username + password
- Add new books to inventory
- Remove or edit book details
- View full book list
- Apply a general discount to all books

### 👩‍💻 User
- Sign up with input validations (email, password, phone number)
- Login with email/username + password
- View all books or apply price filters
- Search books by title
- Purchase multiple books with transaction history
- Auto-update quantities on purchase

---

## 📂 Project Structure

📁 book.store/ ├── BookStore.java # Main class with all logic ├── admin_data.dat # Admin login data (serialized) ├── user_data.dat # Registered user data (serialized) ├── book_data.dat # Book inventory data (serialized) ├── transaction_Record.dat # Purchase transaction logs

All files are stored locally using Java object serialization.

---

## 💻 How to Run

### 🛠 Prerequisites
- JDK 8 or later
- Java IDE or CLI

### ▶️ Run Steps
1. Clone or download the project.
2. Open `BookStore.java` in your Java IDE.
3. Compile and run:
   ```bash
   javac BookStore.java
   java book.store.BookStore
🔄 User Flow
Select User role.

Choose Sign up or Login.

Access book functionalities:

View all books

Filter by price

Search by title

Purchase books

Check out to generate a bill and update inventory.

🔐 Admin Flow
Select Admin role.

Login using credentials from admin_data.dat.

Manage book inventory:

Add/Edit/Delete books

Apply global discounts

View current inventory

📦 Data Storage Format
Example book_data.dat

[["Book Title", "Author", "Quantity", "Price", "Discount"]]
Example user_data.dat

[["FirstName", "LastName", "Username", "Email", "Phone", "Password", "ConfirmPassword"]]
Example transaction_Record.dat

[["Username", "Email", "Phone", "Book Title", "Author", "Quantity", "Book Price", "Total", "Timestamp"]]
Data is stored and read using ObjectOutputStream and ObjectInputStream.

📐 Validation Rules
Email: Must follow standard email format

Phone Number: Must start with +92 followed by 10 digits

Password: Minimum 8 characters with digits and special characters

🧰 Tech Stack
Language: Java

IDE: NetBeans (optional)

Serialization: Java Object Streams

CLI: Console-based interactions via Scanner

🤝 Contributing
Feel free to fork the project and submit pull requests. Contributions are welcome in:

UI enhancements

Data encryption

Bug fixes or optimizations

📄 License
This project is for educational purposes only. No official license applied.

---








