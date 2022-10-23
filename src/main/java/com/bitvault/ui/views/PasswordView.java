package com.bitvault.ui.views;

import com.bitvault.ui.model.Password;
import com.bitvault.ui.components.*;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.util.JavaFxUtil;
import com.bitvault.util.Labels;
import com.bitvault.util.ListNodeConverter;
import com.bitvault.ui.viewmodel.PasswordCardVM;
import com.bitvault.ui.viewmodel.PasswordVM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;


public class PasswordView extends BitVaultVBox {

    private final PasswordVM passwordVM;
    private final ListView<PasswordCardView> listView;

    public PasswordView(PasswordVM passwordVM) {
        this.passwordVM = passwordVM;

        var passwordCards = createPasswordCards(passwordVM.getPasswords());
        ObservableList<PasswordCardView> data = FXCollections.observableArrayList(passwordCards);

        final FilteredList<PasswordCardView> filteredList = new FilteredList<>(data, s -> true);
        listView = new BitVaultListView<>(filteredList);
        listView.setPlaceholder(new Label(Labels.i18n("no.records")));

        final BitVaultCard listOnCard = new BitVaultCard(listView);

        passwordVM.getPasswords().addListener(
                new ListNodeConverter<>(listView, data, this::toCard)
        );


        final WrappedTextField searchTf = new WrappedTextField()
                .withPlaceholder(Labels.i18n("search"))
                .withFilter(filteredList, (passwordCardView, s) -> {
                            var toLower = s.toLowerCase();
                            return passwordCardView.getPassword().username().toLowerCase().contains(toLower)
                                    || passwordCardView.getPassword().secureDetails().domain().toLowerCase().contains(toLower);
                        }
                );

        VBox.setMargin(searchTf, BvInsets.right15);


        final BitVaultFlatButton addNewBtn = new BitVaultFlatButton(Labels.i18n("add.new"))
                .action(event -> showNewPassPopUp());


        final BitVaultFlatButton reloadBtn = new BitVaultFlatButton(Labels.i18n("reload"));
        reloadBtn.setOnAction(
                event -> {
                    passwordVM.reload();
                    listView.scrollTo(0);
                }
        );


        final BitVaultHBox topHBox = new BitVaultHBox(
                addNewBtn,
                reloadBtn,
                searchTf
        );
        topHBox.setMaxHeight(100);


        this.getChildren().addAll(
                topHBox,
                listOnCard
        );


        this.setAlignment(Pos.TOP_CENTER);
        this.setFillWidth(true);
        this.setPadding(BvInsets.all10);
        super.vGrowAlways();
    }

    private List<PasswordCardView> createPasswordCards(
            List<Password> list
    ) {
        return list.stream().map(
                this::toCard
        ).toList();
    }

    private PasswordCardView toCard(Password password) {
        PasswordCardVM passwordCardVM = new PasswordCardVM(password);
        return new PasswordCardView(passwordCardVM, this::showEditPopUp, this::showDeletePopUp);
    }

    private void showNewPassPopUp() {

        final PasswordDetailsView view = PasswordDetailsView.create(
                passwordVM.getCategories(),
                passwordVM.getProfile(),
                passwordVM::create
        );

        JavaFxUtil.popUp(this.getScene().getWindow(), view).show();
    }


    public void showEditPopUp(Password oldPass) {
        final PasswordDetailsView view = PasswordDetailsView.edit(
                oldPass,
                passwordVM.getCategories(),
                passwordVM.getProfile(),
                passwordVM::update
        );

        JavaFxUtil.popUp(this.getScene().getWindow(), view).show();
    }

    public void showDeletePopUp(Password password) {

        passwordVM.delete(password);

    }
}
