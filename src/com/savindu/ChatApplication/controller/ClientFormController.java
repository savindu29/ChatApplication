package com.savindu.ChatApplication.controller;


import com.savindu.ChatApplication.util.Client;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;

public class ClientFormController {


    public static VBox senderVBox;
    public Client client;
    public ScrollPane scrollPane;
    public static String userName="";
    public VBox vBoxMsg;
    public Label lblName;
    public TextField txtMsgBox;

    public void initialize(){
        System.out.println("initialize");
    }

    public void setClientName(String name){
        userName=name;
        new Thread(()->{
            try {
                senderVBox = vBoxMsg;
                lblName.setText(name);
                client = new Client(new Socket("localhost", 8080),
                        name, vBoxMsg);
                System.out.println("Connected to the server");
                //=================
                client.listenForMessage(vBoxMsg, name);
                client.sendMessage(name+" has joined the chat!",vBoxMsg,
                        "SERVER");
            }catch (IOException e){
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
    public void exitClientOnAction(MouseEvent mouseEvent) {
    }
    public static void displayMessageOnRight(String messageToSend,VBox vBox){
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
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBox);
                }
            });
        }
    }
    public static void displayMessageOnLeft(String message,VBox vBox){
        if (!message.isEmpty()){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5,5,5,10));
            Text msgText = new Text(message);
            TextFlow textFlow = new TextFlow(msgText);
            textFlow.setStyle("-fx-background-color: #2ecc71; -fx-background-radius: 10 10 10 0");
            textFlow.setPadding(new Insets(5,10,5,10));
            msgText.setFill(Color.WHITE);
            hBox.getChildren().add(textFlow);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBox);
                }
            });
        }
    }


    public void sendMessageOnAction(MouseEvent mouseEvent) {
        client.sendMessage(txtMsgBox.getText(), vBoxMsg,userName);
        txtMsgBox.clear();
    }
}
