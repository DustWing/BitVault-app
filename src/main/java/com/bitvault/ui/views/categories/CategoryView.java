package com.bitvault.ui.views.categories;

import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.utils.BvSpacing;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Labels;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;

public class CategoryView extends BitVaultVBox {


    private final CategoryVM categoryVM;


    public CategoryView(CategoryVM categoryVM) {
        this.categoryVM = categoryVM;

//        final BvTextField newCategory = new BvTextField()
//                .withBinding(newCategoryName)
//                .withDefaultSize()
//                .withPromptText(Labels.i18n("New Category"));
//
//        final ColorPicker colorPicker = new ColorPicker();
//        colorPicker.setOnAction(event -> newColor = colorPicker.getValue());
//        JavaFxUtil.defaultSize(colorPicker);
//
//        final HBox hBox = new HBox(newCategory, colorPicker);
//        hBox.visibleProperty().bind(showNewCat);
//        hBox.managedProperty().bind(showNewCat);
//        hBox.setAlignment(Pos.CENTER);
//        hBox.setSpacing(BvSpacing.SMALL);
//        JavaFxUtil.hGrowAlways(hBox);


    }

    private void init(){


    }
}
