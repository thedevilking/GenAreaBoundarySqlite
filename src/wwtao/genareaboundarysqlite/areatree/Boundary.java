package wwtao.genareaboundarysqlite.areatree;

import java.util.ArrayList;

/**
 * 
 * @author wwtao
 * 说明：用来存储边界的信息
 * 备注：每一个Line都默认作为一种类型 
 */
public class Boundary extends ArrayList<Line>{
	/**
	 * type表示是属于inner还是outer
	 */
	public int type;
	/**
	 * typeId表示该边界的类型的typeId,也对应着idRole
	 */
	public int id=AreaTree.genId();
}
