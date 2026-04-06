package ru.vlsu.ispi.dao;

import ru.vlsu.ispi.beans.VisualMedia;

public interface VisualMediaDAO {
    void save(VisualMedia visualMedia);
    void update(VisualMedia visualMedia);
    void delete(VisualMedia visualMedia);
    VisualMedia findById(Integer id);
}
