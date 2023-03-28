package com.bitvault.ui.views.categories;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

public class CategoryRowVM {


    private final String id;
    private final SimpleStringProperty categoryName = new SimpleStringProperty();
    private final SimpleObjectProperty<Color> categoryColor = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty allowEdit = new SimpleBooleanProperty(true);
    private final SimpleBooleanProperty allowSave = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty deleted = new SimpleBooleanProperty();


    public CategoryRowVM(String id, String categoryName, Color color) {
        this.id = id;
        this.categoryName.set(categoryName);
        this.categoryColor.set(color);

    }

    public void edit(){
        this.allowEdit.set(!allowEdit.get());
        this.allowSave.set(!allowSave.get());
    }

    public void save(){
        this.allowSave.set(!allowSave.get());
        this.allowEdit.set(!allowEdit.get());
    }

    public String getId() {
        return id;
    }
    public String getCategoryName() {
        return categoryName.get();
    }

    public SimpleStringProperty categoryNameProperty() {
        return categoryName;
    }

    public Color getCategoryColor() {
        return categoryColor.get();
    }

    public SimpleObjectProperty<Color> categoryColorProperty() {
        return categoryColor;
    }

    public boolean isAllowEdit() {
        return allowEdit.get();
    }

    public SimpleBooleanProperty allowEditProperty() {
        return allowEdit;
    }

    public boolean isAllowSave() {
        return allowSave.get();
    }

    public SimpleBooleanProperty allowSaveProperty() {
        return allowSave;
    }

    public boolean isDeleted() {
        return deleted.get();
    }

    public SimpleBooleanProperty deletedProperty() {
        return deleted;
    }

}
