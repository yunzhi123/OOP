#include <iostream>
#include <string>
using namespace std;

class Pet {
    public:
    // constructor, destructor
    Pet(): weight(1), food("Pet chow"){}
    ~Pet() {}

    // accessors
    void setweight(int w) {weight = w;}
    int getweight() {return weight;}

    void setfood(string f) {food = f;}
    string getfood(string f) {return food;}

    // methods
    void eat();
    virtual void speak();

    protected:
    int weight;
    string food;
};

void Pet::eat() {
    cout << "Eating" << food << endl;
}

void Pet::speak() {
    cout << "Growl" << endl;
}

class Rat: public Pet {
    public:
    Rat() {}
    ~Rat() {}

    // method
    void sicken() {cout << "Speading Plague" << endl;}
    void speak();
};

void Rat::speak() {cout << "Rat noise\n";}

class Cat: public Pet {
    public:
    Cat(): number_toes(5) {}
    ~Cat() {}

    // accessor
    void set_toe(int n) {number_toes = n;}
    int get_toe() {return number_toes;}

    // method
    void speak() override {cout<<"meow!~\n";};

    private:
    int number_toes;
};

int main() {
    Pet peter;
    Rat charles;
    Cat mimi;
    Pet* nose = (Pet*)new Cat();

    peter.speak();
    charles.speak();
    ((Pet)mimi).speak();
    nose->speak();
    // charles.setweight(25);
    // cout<<"charles's weight: "<<charles.getweight()<<endl;
}