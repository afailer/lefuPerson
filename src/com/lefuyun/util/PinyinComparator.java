package com.lefuyun.util;

import java.util.Comparator;

import com.lefuyun.bean.City;

public class PinyinComparator implements Comparator<City>{

    public int compare(City o1,City o2){
    	if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
    }
}
