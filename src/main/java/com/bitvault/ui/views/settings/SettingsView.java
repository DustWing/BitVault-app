package com.bitvault.ui.views.settings;

import com.bitvault.security.UserSession;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.grid.BvSimpleGrid;
import com.bitvault.ui.components.grid.GridRow;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.BvWidths;
import com.bitvault.ui.utils.IntegerStringConverter;
import com.bitvault.util.Labels;
import com.bitvault.util.Messages;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.util.List;

public class SettingsView extends BorderPane {

    private final SettingsVM settingsVM;

    public static SettingsView createTest() {
        final UserSession userSession = UserSession.createTest();
        SettingsVM settingsVM = new SettingsVM(userSession);
        return new SettingsView(settingsVM);
    }

    public SettingsView(SettingsVM settingsVM) {
        this.settingsVM = settingsVM;

        final ScrollPane settings = settings();
        final Button button = saveBtn();

        this.setCenter(settings);
        this.setBottom(button);
    }

    public ScrollPane settings() {

        GridRow masterPasswordCoolDown = masterPasswordCoolDown();
        GridRow passwordGenerationLength = passwordGenerationLength();

        BvSimpleGrid settingsGrid = BvSimpleGrid.createDoubleColumnCentered(
                List.of(masterPasswordCoolDown, passwordGenerationLength)
        );


        ScrollPane scrollPane = new ScrollPane(settingsGrid);
        scrollPane.setFitToWidth(true);

        BorderPane.setAlignment(scrollPane, Pos.CENTER);
        BorderPane.setMargin(scrollPane, BvInsets.all20);

        return scrollPane;
    }

    public GridRow masterPasswordCoolDown() {

        Label label = new Label(Labels.i18n("master.password.cooldown"));

        int min = 0;
        int max = 99;

        Spinner<Integer> spinner = new Spinner<>(min, max, 1, 1);
        spinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);

        String tooltipTxt = Messages.i18n("spinner.tooltip").formatted(min, max);
        spinner.setTooltip(new Tooltip(tooltipTxt));
        spinner.setEditable(true);
        spinner.setPrefWidth(BvWidths.SMALL);
        spinner.setMinWidth(BvWidths.SMALL);
        spinner.getValueFactory().valueProperty().bindBidirectional(this.settingsVM.masterPasswordCoolDownProperty());
        IntegerStringConverter.createFor(spinner);

        return new GridRow(List.of(label, spinner));
    }

    public GridRow passwordGenerationLength() {

        Label label = new Label(Labels.i18n("password.generation length"));

        int min = 0;
        int max = 99;

        Spinner<Integer> spinner = new Spinner<>(min, max, 1, 1);
        spinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);

        String tooltipTxt = Messages.i18n("spinner.tooltip").formatted(min, max);
        spinner.setTooltip(new Tooltip(tooltipTxt));
        spinner.setEditable(true);
        spinner.setPrefWidth(BvWidths.SMALL);
        spinner.setMinWidth(BvWidths.SMALL);
        spinner.getValueFactory().valueProperty().bindBidirectional(this.settingsVM.passwordGenerationLengthProperty());
        IntegerStringConverter.createFor(spinner);

        return new GridRow(List.of(label, spinner));
    }

    public Button saveBtn() {
        BvButton button = new BvButton(Labels.i18n("save"))
                .action((event) -> this.settingsVM.save())
                .defaultButton(true)
                .withDefaultSize();

        BorderPane.setAlignment(button, Pos.CENTER);
        BorderPane.setMargin(button, BvInsets.all10);

        return button;
    }

}
