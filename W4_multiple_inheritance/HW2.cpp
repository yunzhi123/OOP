#include<iostream>
using namespace std;

class A {
    // ...
    public:
    A(string _name): name{_name} {
        cout << "A constructor\n";
    }
    ~A() {}

    string name;
};

class B: public virtual A {
    // ...
    public:
    B(): A{"Peter"} {
        cout << "B constructor\n";
    }
    ~B() {}
};

class C: public virtual A {
    // ...
    public:
    C(): A{"Cathy"} {
        cout << "C constructor\n";
    }
    ~C() {}
};

class D: public B, public C {
    // ...
    public:
    D(): A{"Grandpa"} {
        cout << "D constructor\n";
    }
    ~D() {}
};

int main() {
    D* d = new D();
    cout <<d->name;
}