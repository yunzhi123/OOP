#include<iostream>
#include<string.h>
#include<memory>
using namespace std;

class Employee {
    public:
        // constructor
        Employee(const char *name, int id): _id(id), _name(make_unique<char[]> (strlen(name) + 1)) {
            strcpy(_name.get(), name);
        }

        // copy constructor
        Employee(const Employee &rhs): _id(rhs.getId()), _name(make_unique<char[]>(strlen(rhs.getName()) + 1)) {
            strcpy(_name.get(), rhs._name.get());
        }

        // assignment operator
        Employee& operator=(const Employee & rhs) {
            if (this == & rhs) return *this;
            _id = rhs.getId();
            _name = make_unique<char[]>(strlen(rhs.getName()) + 1);
            strcpy(_name.get(), rhs._name.get());
            return *this;
        }

        // methods
        const char *getName() const {return _name.get();}
        int getId() const {return _id;}
    private:
        unique_ptr<char[]> _name;
        int _id;
};


int main() {
    int *a = new int(100);
    cout<<a<<" "<<*a;
}