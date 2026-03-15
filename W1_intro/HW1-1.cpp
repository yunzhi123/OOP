#include<iostream>
#include<string.h>
using namespace std;

class Employee {
    public:
        // constructor
        Employee(const char *name, int id): _id(id), _name(new char[strlen(name) + 1]) {
            strcpy(_name, name);
        }

        // copy constructor
        Employee(const Employee &rhs): _id(rhs.getId()), _name(new char[strlen(rhs.getName()) + 1]) {
            strcpy(_name, rhs._name);
        }

        // assignment operator
        Employee& operator=(const Employee & rhs) {
            if (this == & rhs) return *this;
            char *temp = new char[strlen(rhs.getName()) + 1];
            strcpy(temp, rhs._name);
            delete [] _name;
            _name = temp;
            _id = rhs.getId();
            return *this;
        }
        
        // destructor
        ~Employee() {
            delete [] _name;
        }

        // methods
        const char *getName() const {return _name;}
        int getId() const {return _id;}
    private:
        char *_name;
        int _id;
};


int main() {
    int *a = new int(100);
    cout<<a<<" "<<*a;
}