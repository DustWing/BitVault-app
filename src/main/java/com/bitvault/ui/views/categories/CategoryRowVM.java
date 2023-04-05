package com.bitvault.ui.views.categories;

import com.bitvault.ui.async.AsyncTask;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

import java.util.function.Function;

public class CategoryRowVM {


    private String id;
    private final SimpleStringProperty categoryName = new SimpleStringProperty();
    private final SimpleObjectProperty<Color> categoryColor = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty allowEdit = new SimpleBooleanProperty(true);
    private final SimpleBooleanProperty allowSave = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty loading = new SimpleBooleanProperty();

    private final Function<CategoryRowView, Boolean> onDelete;
    private final Function<CategoryRowView, Result<String>> onSave;


    public CategoryRowVM(
            String id,
            String categoryName,
            Color color,
            Function<CategoryRowView, Boolean> onDelete,
            Function<CategoryRowView, Result<String>> onSave
    ) {
        this.id = id;
        this.onDelete = onDelete;
        this.onSave = onSave;
        this.categoryName.set(categoryName);
        this.categoryColor.set(color);

    }

    public void delete(CategoryRowView categoryRowView) {

        AsyncTask.toRun(() -> onDelete.apply(categoryRowView))
                .onSuccess(aBoolean -> loading.set(false))
                .onFailure(e -> loading.set(false))
                .start();
    }

    public void edit() {
        this.allowEdit.set(false);
        this.allowSave.set(true);
    }

    public void save(CategoryRowView categoryRowView) {
        AsyncTask.toRun(() -> onSave.apply(categoryRowView))
                .onSuccess(this::onSaveSuccess)
                .onFailure(e -> {
                    e.printStackTrace();
                    loading.set(false);
                })
                .start();
    }

    private void onSaveSuccess(Result<String> newId) {
        this.loading.set(false);
        this.allowSave.set(false);
        this.allowEdit.set(true);
        if (!newId.isFail()) {
            this.id = newId.get();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


    public SimpleBooleanProperty allowEditProperty() {
        return allowEdit;
    }


    public SimpleBooleanProperty allowSaveProperty() {
        return allowSave;
    }


    public SimpleBooleanProperty loadingProperty() {
        return loading;
    }
}
