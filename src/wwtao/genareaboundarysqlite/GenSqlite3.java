package wwtao.genareaboundarysqlite;
/** 
 * @author wwtao thedevilking@qq.com: 
 * @version 创建时间：2015-10-22 下午3:14:04 
 * 说明 ：根据已有的区域的树形结构生成sqlite3数据库文件
 * 
 * 所需要的查询操作：
 * String sql="select a.idRef,b.Name from Relation a,RelationRole b " +
				"where a.idRole=b.id and b.Name='subarea' and a.idRelation="+idRelation;
 * String sql="select a.idRelation,b.idWay,b.Position,b.idNode,c.Name,d.lat,d.lon " +
				"from Relation a,Way b,RelationRole c,Node d " +
				"where b.idNode=d.id and a.idRef=b.idWay and a.idRole=c.id and (c.Name='inner' or c.Name='outer')" +
				" and a.idRelation="+idRelation;
 * String sql=String.format("select a.idRelation,b.Name,c.Text " +
				"from KeyValueRelation a,KeyNode b,ValueNode c " +
				"where a.idKey=b.id and a.idVal=c.id and b.Name='name' and a.idRelation=%s",idRelation);
 * 过程：生成数据库表: Relation,RelationRole,Node  ,  KeyValueRelation,KeyNode,ValueNode
 * 	
 * 针对AreaTree：将父类的area的id存储为Relation.idRelation,子类的的id存储为idRef,Type为1,idRole对应到RelationRole中的subarea
 * 依次对所有的父子Area进行上述操作
 * 
 * 针对每一个Area的boundary：针对每一个Area按照顺序，第一个Boundary为outer，后面的为Inner或者outer交替。
 * 将Area的id作为Relation.idRelation,Boundary中的的line的id作为idWay,idRole用这个boundary的属性，对应到RelationRole中为inner或者outer。
 * 将Area中所有boundary的Line都按照上述存储进去
 * 
 * 针对每一个Line: Line的id作为Way.idWay,Line里面的点的id作为idNode,Position就是Line里面点的顺序，从0开始计数
 * 
 * 针对每一个Node: id,经度，维度,对应上即可
 */
public class GenSqlite3 {
	
}
