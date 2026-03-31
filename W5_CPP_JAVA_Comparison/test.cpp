#include<iostream>
using namespace std;
class human {
    public:
    int weight;
    int height;
    virtual void walk() = 0; // pure virtual function
};

int main() {
    // human x;  會錯，有純虛擬函式會變抽象類別，不能直接建立該物件
}
