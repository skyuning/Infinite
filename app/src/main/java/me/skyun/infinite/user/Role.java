package me.skyun.infinite.user;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

import me.skyun.infinite.InfiniteDB;

/**
 * Created by linyun on 16/6/1.
 */

@Table(database = InfiniteDB.class)
public class Role extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public long id;

    @Unique
    @Column
    public String name;

    @Column
    public int age;

    @Column
    public String avatar;
}
