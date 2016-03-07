package wwtao.genareaboundarysqlite;

import java.io.File;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import wwtao.genareaboundarysqlite.areatree.AreaNode;
import wwtao.genareaboundarysqlite.areatree.AreaTree;
import wwtao.genareaboundarysqlite.areatree.Boundary;
import wwtao.genareaboundarysqlite.areatree.Line;
import wwtao.genareaboundarysqlite.areatree.Point;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
 * 过程：生成数据库表: Relation,RelationRole,Node,Way  ,  KeyValueRelation,KeyNode,ValueNode
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
 * 针对每一个Node: id,经度,维度,对应上即可
 * 
 * 针对名字，（）
 * 
 * 
 * 区域的生成会调用AreaTree里面的generTestTree()方法，读取assets文件夹中的aodv.nod.xml进行点的位置的录入
 */
public class GenSqlite3 {
	//数据库
	SQLiteDatabase db;
	
	private static class SingleInstance{
		private static GenSqlite3 instance=new GenSqlite3();
	}
	
	public static GenSqlite3 getInstance(){
		return SingleInstance.instance;
	}
	
	public void createSqlite(Context context){
		
		File parent=context.getDir("databases", Context.MODE_PRIVATE);
		parent=new File(parent.getParent(),"databases");
		File dbfile=new File(parent,"sumoMap.db");
		System.out.println("file name:"+dbfile.getPath());
		if(dbfile.exists()){
			dbfile.delete();
			System.out.println("删除原有的sumoMap.db");
		}
		
		db=context.openOrCreateDatabase("sumoMap.db", Context.MODE_PRIVATE, null);
		
		System.out.println("generate db ");
//		db.close();
		
		//往数据库中写入地图文件
		writeAreaLayertoDb(context);
		System.out.println("write db successfully");
	}
	
	//将AreaTree的信息写入到数据库中
	public void writeAreaLayertoDb(Context context){
		//建立表
		String sql="CREATE TABLE Relation ( id   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,idRelation  INTEGER    NOT NULL,idRef  INTEGER    NOT NULL,Type  INTEGER    NOT NULL,idRole  INTEGER    NOT NULL);";
		db.execSQL(sql);
		sql="CREATE TABLE RelationRole ( id   INTEGER PRIMARY KEY NOT NULL UNIQUE,Name  TEXT  NOT NULL);";
		db.execSQL(sql);
		sql="CREATE TABLE Node ( id   INTEGER PRIMARY KEY NOT NULL UNIQUE,lat  INTEGER NOT NULL,lon  INTEGER NOT NULL );";
		db.execSQL(sql);
		sql="CREATE TABLE KeyValueRelation ( id   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,idRelation   INTEGER NOT NULL ,idKey  INTEGER    NOT NULL,idVal  INTEGER    NOT NULL);";
		db.execSQL(sql);
		sql="CREATE TABLE KeyNode ( id   INTEGER PRIMARY KEY NOT NULL UNIQUE,Name  TEXT    NOT NULL);";
		db.execSQL(sql);
		sql="CREATE TABLE ValueNode ( id   INTEGER PRIMARY KEY NOT NULL UNIQUE,Text  TEXT    NOT NULL);";
		db.execSQL(sql);
		sql="CREATE TABLE Way ( id   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,idWay  INTEGER    NOT NULL,idNode INTEGER    NOT NULL,Position  INTEGER    NOT NULL);";
		db.execSQL(sql);
		
		//写入基本信息
		//RelationRole里面写入subarea,inner,outer," "
		int subarea_id=1;
		int inner_id=2;
		int outer_id=3;
		db.execSQL(String.format("insert into RelationRole(id,Name) values('%d','subarea');",subarea_id));
		db.execSQL(String.format("insert into RelationRole(id,Name) values('%d','inner');",inner_id));
		db.execSQL(String.format("insert into RelationRole(id,Name) values('%d','outer');",outer_id));
		
		//写入层次关系
		try {
			AreaTree.generTestTree(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("GenSqlite3","生成属性区域划分出错");
		}
		AreaNode node=AreaTree.getInstance().root;
		node.id=912940;
		Queue<AreaNode> queue=new LinkedBlockingDeque<AreaNode>();
		queue.add(node);
		//层次遍历这个树
		boolean is_outer=true;
		while(!queue.isEmpty()){
			is_outer=true;
			node=queue.poll();
			
			
			for(AreaNode temp:node.childrenlist){
				db.execSQL(String.format("insert into Relation(idRelation,idRef,Type,idRole) values('%d','%d','1','%d');",node.id,temp.id,subarea_id));
				queue.add(temp);
			}
			
			
			//写入boundary关系，针对每一个AreaNode
			for(Boundary bound:node.boundarylist){
				
				//根据逻辑变量确定是outer还是inner
				if(is_outer)
					bound.type=outer_id;
				else
					bound.type=inner_id;
				
				//对每一个Line,写入relation到line的对应
				for(Line line:bound){
					//将line存入到数据库
					db.execSQL(String.format("insert into Relation(idRelation,idRef,Type,idRole) values('%d','%d','1','%d');",node.id,line.id,bound.type));
					
					//判断该line是否已经存入到数据库
					if(contain(line)){
						System.out.println(String.format("line(%d) 之前已经被处理过了	", line.id));
						continue;
					}
					
					//针对每个Line写入里面的line与point的对应关系
					for(int i=0;i<line.size();i++){
						Point p=line.get(i);
						
						db.execSQL(String.format("insert into Way(idWay,idNode,Position) values('%d','%d','%d')", line.id,p.id,i));
						
						//point写入到点的表中
						double temp=p.longitude*10000000d;
						int lon=(int)temp;
						temp=p.latitude*10000000d;
						int lat=(int)temp;
						
						//判断该point是否已经存入到数据库
						if(contain(p)){
							System.out.println(String.format("point(%d) 之前已经被处理过了	", p.id));
							continue;
						}
						db.execSQL(String.format("insert into Node(id,lat,lon) values('%d','%d','%d')", p.id,lat,lon));
						
						/*String temp_sql=String.format("select id from Node where id=%d",p.id);
						Cursor cursor=db.rawQuery(temp_sql, null);
						if(!cursor.moveToNext()){
							System.out.println(String.format("point(%d)已经在数据库中了", p.id));
							db.execSQL(String.format("insert into Node(id,lat,lon) values('%d','%d','%d')", p.id,lat,lon));
						}
						cursor.close();*/
					}
				}
				
				//更换是outer还是inner
				is_outer=!is_outer;
			}
		}
		
		//清空所有的map
		clearmap();
	}
	
	//生成唯一的AreaNode,Boundary,Line,Point
//	HashMap<Integer,AreaNode> areanodemap=new HashMap<Integer,AreaNode>();
//	HashMap<Integer,Boundary> boundarymap=new HashMap<Integer,Boundary>();
	HashMap<Integer,Line> linemap=new HashMap<Integer,Line>();
	HashMap<Integer,Point> pointmap=new HashMap<Integer,Point>();
	
	//检查这些要放入数据库中的AreaNode,Boundary,Line,Point的唯一性
//	public boolean contain(AreaNode node){
//		if(areanodemap.containsKey(node.id))
//			return true;
//		else
//			areanodemap.put(node.id, node);
//		return false;
//	}
//	
//	public boolean contain(Boundary boundary){
//		if(boundarymap.containsKey(boundary.id)){
//			return true;
//		}
//		else
//			boundarymap.put(boundary.id, boundary);
//		return false;
//	}
	
	public boolean contain(Line line){
		if(linemap.containsKey(line.id))
			return true;
		else
			linemap.put(line.id, line);
		return false;
	}
	
	public boolean contain(Point point){
		if(pointmap.containsKey(point.id))
			return true;
		else
			pointmap.put(point.id, point);
		return false;
	}
	
	public void clearmap(){
//		areanodemap.clear();
//		boundarymap.clear();
		linemap.clear();
		pointmap.clear();
	}
}
