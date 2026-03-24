#include <iostream>
#include <string>

struct IMYourGrandFather {
    std::string name;

    IMYourGrandFather(const std::string& name)
        : name{name} {
        std::cout << "I'm your father's father: " << name << "!!" << std::endl;
    }
};

struct IMYourFatherA : public IMYourGrandFather {
    IMYourFatherA()
        : IMYourGrandFather{"Amanda"} {
        std::cout << "I'm your father A!!" << std::endl;
    }
};

struct IMYourFatherB : public IMYourGrandFather {
    IMYourFatherB()
        : IMYourGrandFather{"Boruto"} {
        std::cout << "I'm your father B!!" << std::endl;
    }
};

struct ItsMe 
    : public IMYourFatherB
    , public IMYourFatherA {

    ItsMe() {
        std::cout << "It's me, Mario!!" << std::endl;

        std::cout << IMYourFatherA::name << std::endl;
        std::cout << IMYourFatherB::name << std::endl;
    }
};

void run_this_function() {
    ItsMe mario;
}

int main()
{
    run_this_function();
    std::cout << "Hello, world!\n";
}