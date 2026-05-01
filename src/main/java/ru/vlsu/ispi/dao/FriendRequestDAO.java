package ru.vlsu.ispi.dao;

import ru.vlsu.ispi.beans.Achievement;
import ru.vlsu.ispi.beans.FriendRequest;

public interface FriendRequestDAO {
    void save(FriendRequest friendRequest);
    void update(FriendRequest friendRequest);
    void delete(FriendRequest friendRequest);
    FriendRequest findById(Integer id);
}
