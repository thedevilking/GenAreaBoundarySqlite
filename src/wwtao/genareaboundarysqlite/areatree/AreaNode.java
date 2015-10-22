package wwtao.genareaboundarysqlite.areatree;

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
	List<Boundary> boundary;
	
	/**
	 * 本节点的唯一id
	 */
	int id=AreaTree.genId();
}
