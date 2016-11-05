package com.unique.app.community.base.recyclerView;

import com.unique.app.community.base.Mvp.BaseActivity;
import com.unique.app.community.base.Mvp.IView;

/**
 * Author: Wamcs
 * mail: kaili@hustunique.com
 * Created on 9/28/16.
 */
public abstract class BaseListActivity<T extends BaseListPresenter> extends BaseActivity<T>
        implements IRefresh,RefreshListener{



    @Override
    public void refreshTop() {
        mPresenter.refreshTop();
    }

    @Override
    public void refreshBottom() {
        mPresenter.refreshBottom();
    }

    @Override
    public boolean isRefreshing() {
        return mPresenter.isRefreshing();
    }


    @Override
    public abstract void onRefreshStateChanged(boolean isRefreshing);

    @Override
    public abstract void onError(Throwable t);
}
