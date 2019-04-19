package com.lefuyun.bean;

import java.util.List;

public class SignConfigBean {
   private double inputMin;
   private double inputMax;
   private double confirmMin;
   private double confirmMax;
   private double yMin;
   private double yMax;
   private double accur;
   private List<ColorBean>color;
   private List<Line>line;
   private double diff;
public double getDiff() {
	return diff;
}
public void setDiff(double diff) {
	this.diff = diff;
}
public List<Line> getLine() {
	return line;
}
public void setLine(List<Line> line) {
	this.line = line;
}
public double getInputMin() {
	return inputMin;
}
public void setInputMin(double inputMin) {
	this.inputMin = inputMin;
}
public double getInputMax() {
	return inputMax;
}
public void setInputMax(double inputMax) {
	this.inputMax = inputMax;
}
public double getConfirmMin() {
	return confirmMin;
}
public void setConfirmMin(double confirmMin) {
	this.confirmMin = confirmMin;
}
public double getConfirmMax() {
	return confirmMax;
}
public void setConfirmMax(double confirmMax) {
	this.confirmMax = confirmMax;
}
public double getyMin() {
	return yMin;
}
public void setyMin(double yMin) {
	this.yMin = yMin;
}
public double getyMax() {
	return yMax;
}
public void setyMax(double yMax) {
	this.yMax = yMax;
}
public double getAccur() {
	return accur;
}
public void setAccur(double accur) {
	this.accur = accur;
}
public List<ColorBean> getColor() {
	return color;
}
public void setColor(List<ColorBean> color) {
	this.color = color;
}
   
}
