package App.view.controls;

import App.model.entity.Specialization;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SpecializationToggle extends AnchorPane implements Initializable {
    @FXML
    private Label title;
    @FXML
    private ToggleButton toggle;

    private static byte[] xml;
    private final Specialization specialization;
    private static final SpecToggleGroup specToggleGroup = new SpecToggleGroup();
    private int index;

    public SpecializationToggle(Specialization specialization) {
        super();
        index = -1;
        this.specialization = specialization;
        load();
        title.setText(specialization.getName());
    }

    private void load() {
        final FXMLLoader loader = new FXMLLoader();
        try {
            if (xml == null) {
                xml = getClass().getResource("SpecializationToggle.fxml")
                        .openStream()
                        .readAllBytes();
            }
            loader.setController(this);
            Node node = loader.load(new ByteArrayInputStream(xml));
            this.getChildren().add(node);
        } catch (IOException ex) {
            System.out.println("Failed to load 'SpecializationToggle.fxml' component: " + ex.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggle.setText("-");
        toggle.setOnAction(EventHandler -> action());
    }

    private void action() {
        if(index < 0) specToggleGroup.add(this);
        else specToggleGroup.remove(this);
    }

    private void updateText() {
        toggle.setText(index > 0 ? Integer.toString(index) : "-");
    }

    public int getIndex() {
        return index;
    }

    public Specialization getSpecialization(){
        return specialization;
    }

    public static void clear(){
        specToggleGroup.clear();
    }

    public static void setOnChooseFirst(EventHandler onChooseFirst) {
        specToggleGroup.onChooseFirst = onChooseFirst;
    }

    public static void setOnLostFirst(EventHandler onLostFirst) {
        specToggleGroup.onLostFirst = onLostFirst;
    }

    private static class SpecToggleGroup {
        private EventHandler onChooseFirst;
        private EventHandler onLostFirst;

        List<SpecializationToggle> toggleList = new ArrayList<>();
        int next = 1;

        void add(SpecializationToggle toggle) {
            if(next == 1 && onChooseFirst != null){
                Event event = new Event(toggle, Event.NULL_SOURCE_TARGET, Event.ANY);
                onChooseFirst.handle(event);
            }
            toggle.index = next++;
            toggle.updateText();
            toggleList.add(toggle);

        }

        void remove(SpecializationToggle toggle) {
            for(int i = 0; i < toggleList.size(); i++){
                if(toggleList.get(i).index > toggle.index){
                    toggleList.get(i).index--;
                    toggleList.get(i).updateText();
                }
            }
            toggleList.remove(toggle);
            toggle.index = -1;
            toggle.updateText();
            next--;
            if(next == 1 && onLostFirst != null){
                onLostFirst.handle(new Event(Event.ANY));
            }
        }

        void clear(){
            for(int i = 0; i < toggleList.size(); i++){
                toggleList.get(i).index = -1;
                toggleList.get(i).updateText();
            }
            toggleList.clear();
            next = 1;
        }
    }
}
