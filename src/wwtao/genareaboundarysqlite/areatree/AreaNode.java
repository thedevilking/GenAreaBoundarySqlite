package wwtao.genareaboundarysqlite.areatree;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author wwtao thedevilking@qq.com: 
 * @version 创建时间：2015-10-22 下午2:04:43 
 * 说明 ： 树的节点
 */
public class AreaNode {
	/**
	 * 本节点的数据
	 */
	public List<Boundary> boundarylist;
	
	public List<AreaNode> childrenlist;
	
	/**
	 * 本节点的唯一id
	 */
	public int id=AreaTree.genId();
	
	public AreaNode(){
		boundarylist=new ArrayList<Boundary>();
		childrenlist=new ArrayList<AreaNode>();
	}
	
	/**
	 * 添加边界
	 * @param boundary
	 */
	public void addBoundary(Boundary boundary){
		boundarylist.add(boundary);
	}
	
	/**
	 * 添加子层次区域
	 */
	public void addChild(AreaNode child){
		childrenlist.add(child);
	}
}
