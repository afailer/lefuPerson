package com.lefuyun.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lefuyun.bean.City;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String TABLE_NAME = "lefuOrg.db";
	
	private static DatabaseHelper sInstance;
	
	/** 
     * cityDao ，每张表对于一个 
     */  
    private Dao<City, Integer> mCityDao;

	private DatabaseHelper(Context context) {
		super(context, TABLE_NAME, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, City.class);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
	} 

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, 
			int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, City.class, true);
			onCreate(database, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}  
	}
	
	/** 
     * 单例获取该Helper 
     *  
     * @param context 
     * @return 
     */  
    public static synchronized DatabaseHelper getHelper(Context context) {  
        if (sInstance == null) {  
            synchronized (DatabaseHelper.class) {  
                if (sInstance == null)  
                    sInstance = new DatabaseHelper(context);  
            }  
        }  
        return sInstance;  
    }
    
    /** 
     * 获得cityDao 
     *  
     * @return 
     * @throws SQLException 
     */  
    public Dao<City, Integer> getCityDao() throws SQLException {  
        if (mCityDao == null) {  
            mCityDao = getDao(City.class);  
        }  
        return mCityDao;  
    }  
  
    /** 
     * 释放资源 
     */  
    @Override  
    public void close() {  
        super.close();  
        mCityDao = null;  
    }  

}
