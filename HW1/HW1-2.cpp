#include<iostream>
using namespace std;

class Bar {
private:
    std::array not_important_;
};

class Foo {
public:
    Foo()
        : bar_{new Bar{}} {}

    // Rule of three!
    Foo(const Foo& rhs) {
        bar_ = new Bar{*rhs.bar_};
    }

    Foo&
    operator=(const Foo& rhs) {
        if (this == &rhs) return *this;
        Bar *temp = new Bar(*rhs.bar_);
        delete bar_;
        bar_ = temp;
        return *this;
    }

    ~Foo() {
        delete bar_;
    }

private:
    // XXX: Well, this is not doing well in practical...
    // but hey, this is just some exercise. Please use
    // std::unique_ptr or std::shared_ptr instead.
    //
    // The Foo object will "own" this Bar pointer.
    Bar* bar_{nullptr};
};
