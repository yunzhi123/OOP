#include <iostream>
using namespace std;

class Table {
    public:
        Table() {
            cout << "constructor" << endl;
        }
        ~Table() {
            cout << "destructor" << endl;
        }
    private:
        char *p;
        int siz;
};

void h() {
    Table T1;
    Table T2 = T1;
    Table T3;
    T3 = T2;
}

int main() {
    h();
    return 0;
}