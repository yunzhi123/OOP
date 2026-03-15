#include <iostream>
using namespace std;

class Pet {
    public:
    Pet() {}
    virtual ~Pet() {}

    virtual void speak() {
        cout << "Growl\n";
    }
};

class Cat: public Pet {
    public:
    Cat() {}
    ~Cat() {}

    void speak() {
        cout << "Moew\n";
    }
};

class Dog: public Pet {
    public:
    Dog() {}
    ~Dog() {}

    void speak() {
        cout << "Wolf\n";
    }
};

void chorus(Pet pet, Pet* petptr, Pet& petref) {
    pet.speak();
    petptr->speak();
    petref.speak();
}

int main() {
    Pet* pet = new Pet();
    cout << "pet singing!!\n";
    chorus(*pet, pet, *pet);

    Cat* cat = new Cat();
    cout << "cat singing!!\n";
    chorus(*cat, cat, *cat);
}