package com.lefuyun.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.lefuyun.bean.City;

public class CityDao {

	private Context context;  
	  
    public CityDao(Context context) {  
        this.context = context;  
    }  
    /**
     * 添加一个城市
     * @param city
     */
    public void add(City city) {  
    	try {
			DatabaseHelper.getHelper(context).getCityDao().createOrUpdate(city);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    /**
     * 批量添加城市
     * @param citys
     */
    public void addAll(List<City> citys) {
    	clearCitys();
    	try {
	    	for (City city : citys) {
	    		DatabaseHelper.getHelper(context).getCityDao().createOrUpdate(city);
			}
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public List<City> findCityByRegionName(String regionName) {
    	try {
	    	Map<String, Object> map = new HashMap<String, Object>();
			map.put("region_name", regionName);
			return DatabaseHelper.getHelper(context).getCityDao().queryForFieldValues(map);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return new ArrayList<City>();
    }
    
    public List<City> findCitysById(long pid) {
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("pid", pid);
    		return DatabaseHelper.getHelper(context).getCityDao().queryForFieldValues(map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return new ArrayList<City>();
    }
    
    public List<City> findCitysOfMain(long pid) {
    	List<City> lists = new ArrayList<City>();
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("pid", pid);
			List<City> list = DatabaseHelper.getHelper(context).getCityDao().queryForFieldValues(map);
			if(list != null && list.size() > 0) {
				for (City city : list) {
					List<City> values = findCitysById(city.getId());
					if(values != null && values.size() > 0) {
						lists.addAll(values);
					}
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return lists;
    }
    /**
     * 清除表内容
     */
    public void clearCitys() {
    	try {
			DatabaseHelper.getHelper(context).getCityDao().queryRaw("delete from tb_city");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
