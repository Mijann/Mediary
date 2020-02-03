package com.app.mijandev.mediary.data.repository;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.app.mijandev.mediary.data.repository.Status.DELETED;
import static com.app.mijandev.mediary.data.repository.Status.ERROR;
import static com.app.mijandev.mediary.data.repository.Status.LOADING;
import static com.app.mijandev.mediary.data.repository.Status.SUCCESS;
import static com.app.mijandev.mediary.data.repository.Status.UPDATED;


public class Resource<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable public final String message;
    int positionUpdated;
    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }


    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }

    public static <T> Resource<T> updating(@Nullable T data) {
        return new Resource<>(UPDATED, data, null);
    }

    public static <T> Resource<T> deleting(@Nullable T data) {
        return new Resource<>(DELETED, data, null);
    }

    public int getPositionUpdated() {
        return positionUpdated;
    }

    public void setPositionUpdated(int positionUpdated) {
        this.positionUpdated = positionUpdated;
    }

    public boolean isSuccess() {
        return status == SUCCESS && data != null;
    }

    public boolean isLoading() {
        return status == LOADING;
    }

    public boolean isDeleted() {
        return status == DELETED;
    }

    public boolean isUpdated() {
        return status == UPDATED;
    }

    public boolean isLoaded() {
        return status != LOADING;
    }
}