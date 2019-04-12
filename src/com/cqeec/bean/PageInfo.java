package com.cqeec.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageInfo<E> {
	//每页最大显示3条数据（默认,根据订单做的）
	private int pageSize=3;
	//当前页数(默认)
	private int curPage=1;
	//总的记录数
	private int pageRecordCount;
	//数据集合
	private List<E> list;
	//访问网址
	private String url;
	//用于封装其他数据（可能不需要这个数据）
	private Map<String, Object> data=new HashMap<>();
	//上面是需要手动封装的数据
	//--------------------------------------------------------------------
	//左偏移量
	private int offset_left=5;
	//右偏移量
	private int offset_right=4;
    //开始按钮数字
    private int startPageButton;
    //结束按钮数字
    private int endPageButton;
    
    
	public PageInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PageInfo( int curPage,int pageSize) {
		super();
		this.pageSize = pageSize;
		this.curPage = curPage;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getOffset_left() {
		return offset_left;
	}
	public void setOffset_left(int offset_left) {
		this.offset_left = offset_left;
	}
	public int getOffset_right() {
		return offset_right;
	}
	public void setOffset_right(int offset_right) {
		this.offset_right = offset_right;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getPageRecordCount() {
		return pageRecordCount;
	}
	public void setPageRecordCount(int pageRecordCount) {
		this.pageRecordCount = pageRecordCount;
	}
	public List<E> getList() {
		return list;
	}
	public void setList(List<E> list) {
		this.list = list;
	}
	
	public int getStartPageButton() {
		calculationNumBtn();
		return startPageButton;
	}
	public int getEndPageButton() {
		return endPageButton;
	}
	/**
	 * 计算总页数
	 * @return
	 */
	public int getPageNumber() {
		//根据总的记录数以及每页显示的最大记录数
		//计算出总页数
		int pageNumber
		=pageRecordCount%pageSize==0
		?pageRecordCount/pageSize
		:pageRecordCount/pageSize+1;
		return pageNumber;
	}
	
	  private void calculationNumBtn(){
		    //最初的start与end		
			startPageButton=curPage-offset_left<1?1:curPage-offset_left;
	        endPageButton=curPage+offset_right>getPageNumber()?getPageNumber():curPage+offset_right;
	        //将start与end进行调试
	        //当总页数大于显示的最大数字分页按钮个数的时候
	        if(getPageNumber()>offset_left+offset_right+1) {
	        	//当开始按钮位置等于1时
	        	if(startPageButton==1) {
	        		//最终结束按钮的位置
	        		endPageButton=endPageButton+offset_left-curPage+1;//补给（end）start应有的按钮却没有的按钮数
	        	}
	        	//当总页数等于结束按钮时
	        	if(endPageButton==getPageNumber()) {
	        		//最终开始按钮的位置
	        		startPageButton=startPageButton-(curPage+offset_right-getPageNumber());//补给（start）end应有却没有的按钮数
	        	}
	        }else//当总页数小于数字分页按钮个数时
	          {
	           endPageButton=getPageNumber();
	           startPageButton=1;
	        }
	  
	  }
}
