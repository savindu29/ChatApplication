package com.savindu.ChatApplication.controller;



import com.savindu.ChatApplication.util.Server;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFormController {


    public ScrollPane scrollPane;
    public VBox vBoxMsg;
    public TextField txtMsgBox;
    public Server server;

    public void initialize() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8080);
                System.out.println("Server Started..");
                displayMessageOnRight("Server Started..");
                server= new Server(serverSocket);
                server.startServer(vBoxMsg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        vBoxMsg.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setVvalue((Double) newValue);
            }
        });


    }


    public static void displayMessageOnRight(String messageToSend){
        if (!messageToSend.isEmpty()){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5,5,5,10));
            Text msgText = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(msgText);
            textFlow.setStyle("-fx-background-color: #1abc9c; -fx-background-radius: 10 10 0 10");
            textFlow.setPadding(new Insets(5,5,5,10));
            msgText.setFill(Color.WHITE);
            hBox.getChildren().add(textFlow);
            // vbox_msgs.getChildren().add(hBox);
        }
    }
    public static void displayMessageOnLeft(String messageFromClient, VBox vBox){
        if (!messageFromClient.isEmpty()){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5,5,5,10));
            Text msgText = new Text(messageFromClient);
            TextFlow textFlow = new TextFlow(msgText);
            textFlow.setStyle("-fx-background-color: #2ecc71; -fx-background-radius: 10 10 10 0");
            textFlow.setPadding(new Insets(5,10,5,10));
            msgText.setFill(Color.WHITE);
            hBox.getChildren().add(textFlow);
            Platform.runLater(()->{
                vBox.getChildren().add(hBox);
            });
        }
    }

    public void shutdownServerOnClick(MouseEvent mouseEvent) {
        server.closeServerSocket();
        Platform.exit();
    }
}
