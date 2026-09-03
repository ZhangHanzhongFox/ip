package fox.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/** Represents one conversation entry with its speaker image and text. */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image image) {
        try {
            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load Fox's dialog view.", exception);
        }
        dialog.setText(text);
        displayPicture.setImage(image);
    }

    /** Returns a right-aligned dialog entry for user input. */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /** Returns a left-aligned dialog entry for Fox's response. */
    public static DialogBox getFoxDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        ObservableList<Node> children = FXCollections.observableArrayList(dialogBox.getChildren());
        Collections.reverse(children);
        dialogBox.getChildren().setAll(children);
        dialogBox.setAlignment(Pos.TOP_LEFT);
        return dialogBox;
    }
}
