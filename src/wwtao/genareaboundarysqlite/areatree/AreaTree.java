package wwtao.genareaboundarysqlite.areatree;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author wwtao
 * 说明 ：用来存储区域的树形分层结构
 */
public class AreaTree {
	/**
	 * 树的根节点
	 */
	AreaNode roote=null;
	
	
	private static class SingleInstance{
		static AreaTree instance=new AreaTree();
	}
	
	public AreaTree getInstance(){
		return SingleInstance.instance;
	}
	
	/**
	 * 区域分层的树是否为空
	 * @return
	 */
	public boolean isEmpty(){
		return roote==null;
	}
	
	/**
	 * 生成测试使用的3个田子型区域
	 */
	public void generTestTree(){
		Point a=new Point(-10, 5);
		Point b=new Point(-5, 5);
		Point c=new Point(0, 5);
		Point d=new Point(5, 5);
		
		Point e=new Point(10, 5);
		Point f=new Point(10, 0);
		Point g=new Point(10, -5);
		Point h=new Point(5, -5);
		
		Point i=new Point(0, -5);
		Point j=new Point(-5, -5);
		Point k=new Point(-10, -5);
		Point l=new Point(-10, 0);
		
		Point m=new Point(0, 0);
		Point n=new Point(-5, 0);
		Point o=new Point(5, 0);
		
		Line ab=new Line();ab.add(a);ab.add(b);
		Line bc=new Line();bc.add(b);bc.add(c);
		Line cm=new Line();cm.add(c);cm.add(m);
		Line mi=new Line();mi.add(m);mi.add(i);
		Line ij=new Line();ij.add(i);ij.add(j);
		Line jk=new Line();jk.add(j);jk.add(k);
		Line kl=new Line();kl.add(k);kl.add(l);
		Line la=new Line();la.add(l);la.add(a);
	}
	
	//***************static变量方法区***********************
	
	/**
	 * 已经生成的id的list,确保每一个id都是唯一的
	 */
	static HashSet<Integer> idlist=new HashSet<Integer>(1024);
	
	/**
	 * 随机数生成器
	 */
	static Random rand=new Random();
	
	/**
	 * 生成一个不重复的id
	 * @return
	 */
	public static int genId(){
		int temp=rand.nextInt();
		while(idlist.contains(temp)){
			temp=rand.nextInt();
		}
		idlist.add(temp);
		return temp;
	}
}
