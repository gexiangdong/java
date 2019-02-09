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

import java.io.IOException;
import java.io.InputStream;

public class DemoWinController {
    @FXML
    private Button okButton;

    @FXML
    private TextField nameField;

    @FXML
    private ImageView imageView;

    public DemoWinController(Button okButton) {
        this.okButton = okButton;
    }

    @FXML
    private void okButtonClicked(ActionEvent event) {
        System.out.println("okButton clicked, " + nameField.getText());
    }

    public DemoWinController(){
        // 这个空构造函数必须有；否则会报错：（很奇怪）
        // Caused by: java.lang.InstantiationException: cn.devmgr.tutorial.client.DemoWinController
        //	at java.lang.Class.newInstance(Class.java:427)
        //	at sun.reflect.misc.ReflectUtil.newInstance(ReflectUtil.java:51)
        //	at javafx.fxml.FXMLLoader$ValueElement.processAttribute(FXMLLoader.java:927)
        //	... 19 more
        //Caused by: java.lang.NoSuchMethodException: cn.devmgr.tutorial.client.DemoWinController.<init>()
        //	at java.lang.Class.getConstructor0(Class.java:3082)
        //	at java.lang.Class.newInstance(Class.java:412)
        //	... 21 more
        //Exception running application cn.devmgr.tutorial.client.JavafxApp
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        System.out.println("initialize  okButton=" + okButton);
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

                System.out.println("OK");
            } else {
                System.out.println("ERROR: " + response.getStatusLine().getStatusCode());
                throw new RuntimeException("返回数据错误");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        nameField.requestFocus();
    }


}
