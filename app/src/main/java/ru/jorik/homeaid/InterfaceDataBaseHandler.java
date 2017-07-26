package ru.jorik.homeaid;

import java.util.List;

/**
 * Created by 111 on 26.07.2017.
 */

public interface InterfaceDataBaseHandler<T> {
    public void createItem(T t);
    public T readItem(int id);
    public int updateItem(int id, T t);
    public void deleteItem(int id);

    public void addGroup(List<T> tList);
    public T[] readGroup(int startId, int length);
    public int updateGroup(int startId, List<T> tList);
    public int deleteGroup(int startId, int length);

    public int getCount();
    public List<T> readAll();
    public void deleteAll();
}
