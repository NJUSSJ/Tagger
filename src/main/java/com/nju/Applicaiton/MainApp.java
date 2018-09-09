package com.nju.Applicaiton;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sun.applet.Main;

/**
 * @Author shisj
 * @Date: 2018/9/4 20:30
 */
public class MainApp extends Application {

    public static final String APP_ID = "11786132";
    public static final String API_KEY = "iK5CpBGE5ZZEDIrdvUHN837a";
    public static final String SECRET_KEY = "yF6fC3M6kRGl5y8W2NzqPj5nGunVmFRi";
    public static Stage primaryStage;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainApp.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/Main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css/jfoenix-components.css").toExternalForm());
        primaryStage.setTitle("Tagger");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
