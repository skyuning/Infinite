package me.skyun.infinite;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by linyun on 16/6/1.
 */

@Database(name = InfiniteDB.NAME, version = InfiniteDB.VERSION)
public class InfiniteDB {

    public static final String NAME = "Parallel";
    public static final int VERSION = 1;
}
