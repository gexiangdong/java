# Javafx

javafx 是 oracle 发布的新一代客户端 GUI API，可用于取代 AWT SWING 来编写客户端java程序。 从 JDK7 开始被集成在 JDK 中， 但从 JDK 11 开始分离出来一个独立项目 [OpenJFX](https://openjfx.io) 。



## 使用FXML

.fxml 文件是是 javafx 项目中节目配置文件，可以通过图形编辑（例如idea自带的）来编辑此文件，拖拽方式添加、调整控件位置。

在 .fxml 文件中，通过 `fx:controller` 属性，把 .fxml 和相对应的 java 类关联起来， 在对应的 java 类中可以通过 `@FXML` 注解实现两者之间的控件绑定。

## JFX 的 modules （自 Java 9 始）

  * javafx.base
  * javafx.controls
  * javafx.fxml
  * javafx.graphics
  * javafx.media
  * javafx.swing
  * javafx.web WebView相关的API

## Webview 

javafx 内置的 webview 控件使用 webkit 内核，支持 HTML5， (比 swing 中的 webview 只支持到 HTML3.5 要好太多了。)


## JDK 11 and above

由于从 JDK 11 起JFX已经被分离成独立项目，需要在 pom.xml 中增加依赖：

```xml

<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>11.0.2</version>
</dependency>

```