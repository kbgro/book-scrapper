# BOOKS TO SCRAPE

Web crawler for [books to scrape](https://books.toscrape.com/) site created using Java.

## Setup DB (MYSQL)

```shell
CREATE DATABASE books;
```

### Environmental Variables

```shell
set MYSQL_DATABASE="books"
set MYSQL_USER="your_username"
set MYSQL_PASSWORD="your_password"
```

### Usage
```bash
Books Scrapper CLI v2.0.0

Crawl: options
-c   	category     	Scrape books by category
-page	page no      	Scrape books by page from page 1-50
-all 	             	Scrape all books
-l   	limit        	Limit of books to scrape e.g 10
-lc  	list category	List available categories

Storage: options
-o   	output    	Output to a csv file
-p   	password  	MySQL password
-u   	user      	MySQL user
-db  	database  	MySQL database
-port	database  	MySQL port, 3306 is used by default
-host	database  	MySQL hot, localhost is used by default

Examples
Scrape 5 History books
	java -jar book-v2.0.0.jar -c History -l 5 -o history-books.csv

Scrape 5 books from page 22
	java -jar book-v2.0.0.jar -page 22 -l 5 -o page22-books.csv

SScrape all books
	java -jar book-v2.0.0.jar -all -o page22-books.csv

Scrape all books and save to database
	java -jar book-v2.0.0.jar -all -p password -u user -db books

Scrape 5 Fiction books and save to database providing host and port
	java -jar book-v2.0.0.jar -all -host 127.0.0.1 -p 13306 -p password -u user -db books
	
```
