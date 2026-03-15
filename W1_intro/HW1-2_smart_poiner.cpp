#include<iostream>
#include<memory>
using namespace std;

class Bar {
private:
    std::array not_important_;
};

class Foo {
public:
    Foo(): bar_{new Bar{}} {}

    // Rule of three!
    Foo(const Foo& rhs): bar_(make_unique<Bar>()) {}

    Foo& operator=(const Foo& rhs) {
        if (this == &rhs) return *this;
        bar_ = make_unique<Bar>(*rhs.bar_);
        return *this;
    }

private:
    // XXX: Well, this is not doing well in practical...
    // but hey, this is just some exercise. Please use
    // std::unique_ptr or std::shared_ptr instead.
    //
    // The Foo object will "own" this Bar pointer.
    unique_ptr<Bar> bar_;
};
