package com.acorn.basemodule.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * https://stackoverflow.com/questions/53382320/boundservice-livedata-viewmodel-best-practice-in-new-android-recommended-arc
 * 用于创建ViewModel的Service,不过这种方式很可疑.因为:
 * we won't require LiveData to update to from service and even no ViewModel
 * (Why do we need it for service? We don't need configuration changes to survive on service lifecycle.
 * And main task to VM is that to consist data between lifecycles).
 */
public class LifecycleAndViewStoreOwnerService extends LifecycleService implements ViewModelStoreOwner, HasDefaultViewModelProviderFactory {

    final ViewModelStore mViewModelStore = new ViewModelStore();
    ViewModelProvider.Factory mFactory;

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return mViewModelStore;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (source.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
                    mViewModelStore.clear();
                    source.getLifecycle().removeObserver(this);
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return mFactory != null ? mFactory : (mFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication()));
    }
}