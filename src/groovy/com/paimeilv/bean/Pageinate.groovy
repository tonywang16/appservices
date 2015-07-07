package com.paimeilv.bean

class Pageinate<E> {
	Integer pageSize=48//分页个数
	Integer pagesNo=1//记录当前分页
	Integer pageCount//总页数
	Integer rowCount//总数据量
	
	List<E> result//返回数据
	
	public Integer getRowStart(){
		return (pagesNo-1)*pageSize
	}
	public Integer getRowEnd(){
		Integer rowEnd = (pagesNo*pageSize)
		if(rowEnd>rowCount) rowEnd=rowCount
		return rowEnd
	}
	public Integer getPageCount(){
		Integer pageNum = 1
		if(rowCount&&0!=rowCount){
			pageNum = rowCount/pageSize
			if(rowCount%pageSize>0) pageNum+=1
		}
		return pageNum
	}
	private List<Integer> getPageCountList(){
		Integer pageNum = 1
		List<Integer> pageCountList = new ArrayList<Integer>()
		if(0!=rowCount){
			pageNum = rowCount/pageSize
			if(rowCount%pageSize>0) pageNum+=1
		}
		for(int i=0;i<pageNum;i++){
			pageCountList.add(i)
		}
		return pageCountList
	}
	private List<Integer> getPageCountListByDesc(){
		Integer pageNum = 1
		List<Integer> pageCountList = new ArrayList<Integer>()
		if(0!=rowCount){
			pageNum = rowCount/pageSize
			if(rowCount%pageSize>0) pageNum+=1
		}
		for(int i=pageNum-1;i>=0;i--){
			pageCountList.add(i)
		}
		return pageCountList
	}
	
	public Integer getResultCount(){
		if(!result) return 0
		else return result.size()
	}
}
