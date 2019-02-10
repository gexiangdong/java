package cn.devmgr.tutorial.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

}
