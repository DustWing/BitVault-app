package com.bitvault.ui.views.factory;

import com.bitvault.services.factory.IServiceFactory;
import com.bitvault.services.factory.LocalServiceFactory;
import com.bitvault.ui.viewmodel.DashBoardVM;
import com.bitvault.ui.views.DashBoardView;

public class ViewFactory {

    private final IServiceFactory serviceFactory;


    public ViewFactory createLocal(String location, String filename) {

        var filepath = "%s/%s.vault".formatted(location, filename);

        final LocalServiceFactory localServiceFactory = new LocalServiceFactory(filepath);

        return new ViewFactory(localServiceFactory);

    }

    public ViewFactory(IServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }


    public DashBoardView getDashboardView() {
        return new DashBoardView(
                new DashBoardVM(serviceFactory)
        );

    }


}
