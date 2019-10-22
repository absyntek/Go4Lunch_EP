package com.EtiennePriou.go4launch.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.EtiennePriou.go4launch.ui.chat.ChatViewModel;
import com.EtiennePriou.go4launch.ui.details.DetailsViewModel;
import com.EtiennePriou.go4launch.ui.MainViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)){
            MainViewModel mainViewModel = new MainViewModel();
            if (modelClass.isInstance(mainViewModel)){
                return modelClass.cast(mainViewModel);
            }else throw new IllegalArgumentException("Unknown ViewModel class");
        }else if (modelClass.isAssignableFrom(DetailsViewModel.class)){
            DetailsViewModel detailsViewModel = new DetailsViewModel();
            if (modelClass.isInstance(detailsViewModel)){
                return modelClass.cast(detailsViewModel);
            }else throw new IllegalArgumentException("Unknown ViewModel class");
        }else if (modelClass.isAssignableFrom(ChatViewModel.class)) {
            ChatViewModel chatViewModel = new ChatViewModel();
            if (modelClass.isInstance(chatViewModel)) {
                return modelClass.cast(chatViewModel);
            } else throw new IllegalArgumentException("Unknown ViewModel class");
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
