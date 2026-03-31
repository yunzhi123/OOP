# Description
## 繼承的重點
### 不能亂用繼承
- 可以用 is has 去判斷
### 舉例
- is American a human ?
是人類，所以可以用繼承
- is employee_cencus a list container ?
不是，所以不能用繼承，只能用他的方法，這個叫 composition
```cpp
class EmployeeCensus {
private:
    ListContainer _list; // 我「擁有」一個列表，但我「不告訴」外面我用什麼存
public:
    void AddEmployee(Employee e) { _list.Add(e); }
    // 這裡只提供業務相關的方法，不提供 FirstItem() 或 NextItem() 這種結構操作
};
```
### 另外一個例子
```cpp
#include <iostream>
#include <memory>

// 1. 定義一個「插槽標準」（抽象基底類別）
class Protocol {
public:
    virtual void send(std::string data) = 0; // 純虛擬函式，強迫子類別實作
    virtual ~Protocol() {} 
};

// 2. 製作各種「卡帶」（具體實作）
class Tcp : public Protocol {
public:
    void send(std::string data) override { std::cout << "TCP 傳送: " << data << "\n"; }
};

class Rs232 : public Protocol {
public:
    void send(std::string data) override { std::cout << "RS232 傳送: " << data << "\n"; }
};

// 3. 通用主機（這就是右下角的 myreallygreattm）
class Transmitter {
private:
    Protocol* m; // 這就是那個「插槽」，它可以指著任何繼承 Protocol 的東西
public:
    Transmitter(Protocol* p) : m(p) {} // 透過建構子把卡帶插進去

    void work(std::string d) {
        // 這裡就是圖中 dosomething1 的地方
        // 邏輯只寫一次，不管插什麼卡都能跑
        std::cout << "準備傳輸...\n";
        m->send(d); 
        std::cout << "傳輸完成。\n";
    }
};

int main() {
    // 這裡就是右下角那兩行 new
    Transmitter t1(new Tcp());
    t1.work("Hello World");

    Transmitter t2(new Rs232());
    t2.work("Hello World");
}
```
### override
- 需要注意的是在子類別 override 父類別會直接覆蓋掉該同名的 func，所以要嘛直接全部 override 要嘛用 virtual

