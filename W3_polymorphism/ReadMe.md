## 多型
### 定義
- 多型就是當你用基底 class 的指標或參照來進行處理時，卻可以依照其 subclass 來進行不一樣的事情
### virtual function
- 任何想要被繼承的 class overriden 的 method 都要宣告成 virtual
- 一個 func 一旦宣告成virtual，則在繼承的 class 也是 virtual，可以忽略不寫，不過建議要寫
- 多型只對 pointer, reference, 以及宣告為virtual 的 func 有效
- 如果一個　class 有任何一個method 被宣告成virtual，則destructor也應該宣告成 virtual，確保 destructor有做正確的清除