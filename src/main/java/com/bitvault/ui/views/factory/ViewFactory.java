package com.bitvault.ui.views.factory;

import com.bitvault.security.UserSession;
import com.bitvault.services.factory.IServiceFactory;
import com.bitvault.ui.views.dashboard.DashBoardVM;
import com.bitvault.ui.views.dashboard.DashBoardView;

public class ViewFactory {

    private final UserSession userSession;
    private final IServiceFactory serviceFactory;


    public ViewFactory(UserSession userSession, IServiceFactory serviceFactory) {
        this.userSession = userSession;
        this.serviceFactory = serviceFactory;
    }


    public DashBoardView getDashboardView() {
        return new DashBoardView(
                new DashBoardVM(userSession, serviceFactory)
        );

    }


}
