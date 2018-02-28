package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 28/02/2018.
 */

class Cancelled {
    private boolean isCancelled;

    public Cancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
