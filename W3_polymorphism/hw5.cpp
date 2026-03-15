#include <stdio.h>
#include <assert.h>
#include<iostream>
#include<vector>
#define SIZE 3
using namespace std;
//book_type
    // Comic,
    // Novel,
    // Magazine
class Book {
    public:
    virtual void print_book() = 0;
    virtual ~Book() {}
};

class Comic: public Book {
    public:
    void print_book() {
        cout << "Comic\n";
    }
    ~Comic() {}
};

class Novel: public Book {
    public:
    void print_book() {
        cout << "Novel\n";
    }
    ~Novel() {}
};

class Magazine: public Book {
    public:
    void print_book() {
        cout << "Magazine\n";
    }
    ~Magazine() {}
};

int main() {
    vector<Book*> list = {new Comic(), new Novel(), new Magazine()};
    for (auto& type: list) type->print_book();
    for (auto& i: list) {
        delete i;
    }
}
