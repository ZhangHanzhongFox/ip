package fox.gui;

import fox.Fox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/** Controls Fox's main conversation window and delegates commands to Fox. */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Fox fox;
    private final Image userImage = new Image(MainWindow.class.getResourceAsStream("/images/User.png"));
    private final Image foxImage = new Image(MainWindow.class.getResourceAsStream("/images/Fox.png"));

    /** Binds the conversation view to the existing Fox application logic. */
    public void setFox(Fox fox) {
        this.fox = fox;
        dialogContainer.getChildren().add(DialogBox.getFoxDialog(
                "Hi! I'm Fox. What can I do for you?", foxImage));
    }

    /** Keeps the latest conversation entry visible. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Displays the user's command and Fox's response, then clears the input. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }
        String response = fox.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getFoxDialog(response, foxImage));
        userInput.clear();
        if (input.equalsIgnoreCase("bye")) {
            Platform.exit();
        }
    }
}
