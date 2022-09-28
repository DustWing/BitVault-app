package com.bitvault.views.factory;

import com.bitvault.services.factory.IServiceFactory;
import com.bitvault.viewmodel.DashBoardVM;
import com.bitvault.viewmodel.PasswordVM;
import com.bitvault.views.DashBoardView;
import com.bitvault.views.PasswordView;

public class ViewFactory {

    private final IServiceFactory serviceFactory;


    public ViewFactory(IServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }


    public DashBoardView getDashboardView() {
        return new DashBoardView(
                new DashBoardVM(serviceFactory)
        );

    }

}
