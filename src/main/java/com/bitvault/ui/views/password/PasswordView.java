package com.bitvault.ui.views.password;

import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.listcell.TextColorListCell;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.views.categories.CategoryVM;
import com.bitvault.ui.views.categories.CategoryView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.function.Supplier;


public class PasswordView extends BorderPane {

    private final PasswordVM passwordVM;
    private final PasswordTableView passwordTableView;
    private final Button showPassword;


    public PasswordView(PasswordVM passwordVM) {
        this.passwordVM = passwordVM;

        this.passwordTableView = new PasswordTableView(this.passwordVM);

        this.showPassword = new BvButton("Passwords")
                .withDefaultSize()
                .action(event -> onPasswordAction());

        this.setLeft(leftBox());
        this.setCenter(passwordTableView);
        BorderPane.setMargin(passwordTableView, BvInsets.all10);

    }

    public void onNewCatAction() {
        CategoryVM categoryVM = new CategoryVM(this.passwordVM.getCategoryService());
        CategoryView categoryView = new CategoryView(categoryVM);
        this.setLeft(showPassword);
        this.setCenter(categoryView);
        BorderPane.setMargin(categoryView, BvInsets.all10);
    }

    public void onPasswordAction() {
        passwordVM.init();// to reload passwords
        this.passwordTableView.filterByCategory("ALL");
        this.setLeft(leftBox());
        this.setCenter(passwordTableView);
        BorderPane.setMargin(passwordTableView, BvInsets.all10);
    }

    public VBox leftBox(){
        ListView<Category> categories = categories();
        Button button = newCategoryBtn();

        VBox vBox = new VBox(button, categories);
        JavaFxUtil.vboxArrangement(vBox);
        JavaFxUtil.vGrowAlways(vBox);
        return vBox;
    }

    public Button newCategoryBtn(){

       return new BvButton("New cat")
                .withDefaultSize()
                .action(event -> onNewCatAction());
    }
    public ListView<Category> categories(){
        Category fake = Category.createFake("ALL", "ALL", "#fc0000");
        ObservableList<Category>list = FXCollections.observableArrayList(fake);
        list.addAll(this.passwordVM.getCategories());
        ListView<Category> listView = new ListView<>(list);
        listView.getSelectionModel().select(0);
        final Supplier<Shape> shape = () -> new Circle(5);
        listView.setCellFactory(param -> new TextColorListCell<>(shape.get()));
        listView.setOnMouseClicked(
                event -> {
                    if(event.getButton().equals(MouseButton.PRIMARY)){
                        Category selectedItem = listView.getSelectionModel().getSelectedItem();
                        if(selectedItem!=null){
                            this.passwordTableView.filterByCategory(selectedItem.id());
                        }

                    }
                }
        );
        return listView;
    }

}
