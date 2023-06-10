package com.bitvault.database.daos;

import com.bitvault.database.models.SettingsDM;

import java.util.Collection;
import java.util.List;

public interface ISettingsDao {

    List<SettingsDM> findAll();

    int insert(Collection<SettingsDM> values);

    int update(Collection<SettingsDM> values);

}
