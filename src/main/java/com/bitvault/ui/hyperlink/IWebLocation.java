package com.bitvault.ui.hyperlink;

import javafx.beans.property.SimpleStringProperty;

public interface IWebLocation {
     String getDomain();

     SimpleStringProperty domainProperty();
}
