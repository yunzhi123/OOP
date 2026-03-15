#include <iostream>
#include <vector>
using namespace std;

class Animal {
public:
    virtual void speak() = 0;
    virtual ~Animal() {}
};
class Cat : public Animal {
public:
    void speak() {
        cout << "meow" << endl;
    }
};
class Cow : public Animal {
public:
    void speak() {
        cout << "moo" << endl;
    }
};

int main() {
    vector<Animal*> animals = {new Cat(), new Cow()};
    for (auto& i: animals) i->speak();
    for (auto& i: animals) delete i;
}