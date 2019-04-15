# Junit测试JavaFX程序

如果JUnit的TestCase里涉及到javafx的内容，会报错：

    java.lang.IllegalStateException: Not on FX application thread; currentThread = main

无法测试。


可以通过以下方式解决：

### pom增加依赖

```xml
    <dependency>
      <groupId>de.saxsys</groupId>
      <artifactId>jfx-testrunner</artifactId>
      <version>1.2</version>
    </dependency>
```
jfx-testrunner的github地址： https://github.com/sialcasa/jfx-testrunner

### 编写测试用例

1. 测试用例的类上增加注解 `@RunWith(JfxRunner.class)`
2. 测试用例的方法上增加注解 `@TestInJfxThread`


