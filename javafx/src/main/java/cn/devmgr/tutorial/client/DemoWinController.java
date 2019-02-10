package cn.devmgr.tutorial.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;


/**
 * 和 javafxdemowin.fxml 相结合的控制器
 */
public class DemoWinController {
    private static final Logger logger = LoggerFactory.getLogger(DemoWinController.class);

    @FXML
    private Button okButton;

    @FXML
    private TextField nameField;

    @FXML
    private ImageView imageView;




    /**
     * 初始化这个 controller 类，这个方法当 fxml 文件被加载，本类的 @FXML 注解设置的成员变量都被初始化后执行。
     * 因此可以在此方法里调用其他带有 @FXML 注解的成员变量, 并设置/修改他们的属性（如果尝试在构造函数中修改会得到null）。
     */
    @FXML
    private void initialize() {
        logger.trace("initialize  okButton={}", okButton);
        showImage();
    }

    private void showImage(){
        HttpClientContext context = HttpClientContext.create();
        try(
                CloseableHttpClient client = HttpClientBuilder.create().build();
        ) {
            HttpGet requestLoginPage = new HttpGet("https://persons.shgjj.com");
            HttpResponse response = client.execute(requestLoginPage, context);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //ok 200
                HttpGet reqImg = new HttpGet("https://persons.shgjj.com/img");
                HttpResponse respImg = client.execute(reqImg, context);
                InputStream is = respImg.getEntity().getContent();
                Image image = new Image(is);
                is.close();
                imageView.setImage(image);

                logger.trace("OK");
            } else {
                logger.warn("ERROR: {}", response.getStatusLine().getStatusCode());
                throw new RuntimeException("返回数据错误");
            }
        }catch (Exception e){
            logger.error("ERROR", e);
        }
        nameField.requestFocus();
    }


    @FXML
    private void okButtonClicked(ActionEvent event) {
        logger.trace("okButton clicked, {}", nameField.getText());
    }


    @FXML
    private void openBrowserState(ActionEvent event){
        logger.trace("openBrowserSate");
        Stage browserStage = new Stage();
        browserStage.setWidth(1000);
        browserStage.setHeight(600);
        browserStage.setAlwaysOnTop(true);
        Scene scene = new Scene(new Group());

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

//        ScrollPane scrollPane = new ScrollPane();
//        scrollPane.setContent(browser);

        webEngine.getLoadWorker().stateProperty()
                .addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        logger.trace("changed.....ObservableValue: {}", ov.getValue());
                        if (newState == Worker.State.SUCCEEDED) {
                            browserStage.setTitle(webEngine.getLocation());
                            //可以通过webengine.executeScript方法执行页面内javascript，改变页面内容等等
                            webEngine.executeScript("document.body.innerHTML += '<div style=\"border:20px solid red\"><b>SomeText</b></div>';document.getElementById('main').style.border='20px solid blue';");

                        }

                    }
                });
        webEngine.load("http://devmgr.cn");
        scene.setRoot(browser);
        browserStage.setScene(scene);
        browserStage.show();
    }
}
