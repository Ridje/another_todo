package com.example.anothertodo.observer;

import java.util.ArrayList;
import java.util.List;

public class NotelistHolder {

    private List<NotelistObserver> observers;   // Все обозреватели

    public NotelistHolder() {
        observers = new ArrayList<>();
    }

    // Подписать
    public void subscribe(NotelistObserver observer) {
        observers.add(observer);
    }

    // Отписать
    public void unsubscribe(NotelistObserver observer) {
        observers.remove(observer);
    }

    public void unsubscribeAll() {
        observers.clear();
    }

    public void notifyItemRemoved() {
        for (NotelistObserver observer : observers) {
            observer.notifyNotelistChanged();
        }
    }
}
