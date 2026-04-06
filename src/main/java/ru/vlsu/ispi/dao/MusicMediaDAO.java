package ru.vlsu.ispi.dao;

import ru.vlsu.ispi.beans.MusicMedia;

public interface MusicMediaDAO {
    void save(MusicMedia musicMedia);
    void update(MusicMedia musicMedia);
    void delete(MusicMedia musicMedia);
    MusicMedia findById(Integer id);
}
