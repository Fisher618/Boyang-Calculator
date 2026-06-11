# AppleStyleCalculator

## 项目简介

AppleStyleCalculator 是一个 Java 课程作业项目，目标是实现一个参考 Apple Calculator 暗色圆角界面风格的多模式桌面计算器。当前版本重点完成项目骨架、基础界面、模式切换和基础计算器的最小可运行功能。

## 技术栈

- Java 17+
- JavaFX
- Maven
- FXML
- CSS

## 项目结构

```text
src/main/java/com/boyang/calculator
├── MainApp.java
├── controller
│   ├── MainController.java
│   ├── BasicController.java
│   ├── ScientificController.java
│   ├── ProgrammerController.java
│   └── HistoryController.java
├── engine
│   ├── BasicCalculatorEngine.java
│   ├── ScientificCalculatorEngine.java
│   ├── ProgrammerCalculatorEngine.java
│   └── ExpressionEvaluator.java
├── model
│   ├── CalculatorMode.java
│   ├── AngleMode.java
│   ├── NumberBase.java
│   ├── CalculationRecord.java
│   └── CalculatorState.java
├── service
│   └── HistoryService.java
└── util
    ├── AnimationUtil.java
    ├── BigNumberUtil.java
    ├── FullResultDialog.java
    ├── FormatUtil.java
    └── ValidationUtil.java

src/main/resources/com/boyang/calculator
├── fxml
│   ├── main.fxml
│   ├── basic.fxml
│   ├── scientific.fxml
│   ├── programmer.fxml
│   └── history.fxml
└── css
    └── apple-dark.css
```

## 运行方式

在项目根目录执行：

```bash
mvn javafx:run
```

## 当前已完成功能

- Maven JavaFX 项目骨架
- `main.fxml` 主界面加载
- `apple-dark.css` 暗色圆角样式
- 基础、科学、程序员三种模式页面切换
- 左侧历史记录面板显示与隐藏
- 基础计算器数字输入、AC、DEL、正负号、百分号、小数点
- 基础计算器使用 BigDecimal 支持超大数加、减、乘、除、百分号和正负号
- 科学计算器支持 BigDecimal 表达式四则运算、括号、整数指数、平方和开方
- 程序员计算器使用 BigInteger 支持超大整数进制转换和位运算
- 主显示区过长时显示省略号，点击显示区域可查看和复制完整结果

## 超大数运算支持说明

1. 基础计算器使用 BigDecimal 作为主要计算类型，支持超大整数和高精度小数，输入数字通过字符串构造 BigDecimal。
2. 程序员计算器使用 BigInteger 作为主要计算类型，支持超大整数的 BIN、OCT、DEC、HEX 进制转换，以及 AND、OR、XOR、NOT、左移、右移等位运算。
3. 主显示区结果过长时会显示省略号，但真实结果会完整保存在控制器中；点击显示区域可以打开“完整计算结果”窗口查看和复制完整结果。
4. 科学函数中的三角函数和对数函数暂时使用 Math 库近似计算，超大数高精度主要支持四则运算、幂运算、开方和程序员整数运算。
5. 当前版本暂时不实现历史记录持久化和平滑切换动画。

## 手动测试记录

基础计算器：

- `999999999999999999999999999999 + 1`：期望 `1000000000000000000000000000000`
- `123456789123456789123456789 * 987654321987654321987654321`：期望完整整数结果，不溢出，不显示 `Infinity`，不使用科学计数法
- `1 / 3`：期望约 80 位高精度小数，不崩溃
- `1000000000000000000000000000000 - 1`：期望 `999999999999999999999999999999`

程序员计算器：

- DEC 输入 `340282366920938463463374607431768211455`，切换 HEX：期望 `FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF`
- HEX 输入 `FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF`，切换 DEC：期望 `340282366920938463463374607431768211455`
- DEC 输入 `1 << 100`：期望 `1267650600228229401496703205376`

完整结果查看：

- 输入特别长的计算结果后，主显示区可以显示省略号
- 点击显示区后弹窗显示完整数字
- “复制结果”按钮复制完整结果
- 下一次计算继续使用完整结果，不使用带省略号的缩略文本

## 预留接口和后续计划

- 科学计算器三角函数和对数函数目前是 Math 近似计算，后续可替换为高精度实现
- 历史记录当前为内存服务，持久化保存后续实现
- 表达式解析器后续可继续扩展函数、常量和更完整的错误提示
- 模式切换动画目前为简单淡入淡出，后续可继续优化

## 运行前注意

本机需要安装 JDK 17 或更高版本，并安装 Maven 或将 Maven 加入 PATH。当前项目不使用数据库，也未引入复杂第三方库。
