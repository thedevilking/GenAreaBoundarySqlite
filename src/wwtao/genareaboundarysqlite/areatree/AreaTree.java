package wwtao.genareaboundarysqlite.areatree;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;

/**
 * 
 * @author wwtao
 * 说明 ：用来存储区域的树形分层结构
 */
public class AreaTree {
	/**
	 * 树的根节点
	 */
	public AreaNode root=null;
	
	
	private static class SingleInstance{
		static AreaTree instance=new AreaTree();
	}
	
	public static AreaTree getInstance(){
		return SingleInstance.instance;
	}
	
	/**
	 * 区域分层的树是否为空
	 * @return
	 */
	public boolean isEmpty(){
		return root==null;
	}
	
	/**
	 * 生成测试使用的3个田子型区域
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static void generTestTree(Context context) throws IOException, ParserConfigurationException, SAXException{
		//定义所需要的测试点
		/*Point a=new Point(-10, 5);
		Point b=new Point(-5, 5);
		Point c=new Point(0, 5);
		Point d=new Point(5, 5);
		Point e=new Point(10, 5);
		
		Point l=new Point(-10, 0);
		Point n=new Point(-5, 0);
		Point m=new Point(0, 0);
		Point o=new Point(5, 0);
		Point f=new Point(10, 0);
		
		Point g=new Point(10, -5);
		Point h=new Point(5, -5);
		Point i=new Point(0, -5);
		Point j=new Point(-5, -5);
		Point k=new Point(-10, -5);*/
		
		InputStream ins=context.getResources().getAssets().open("aodv.nod.xml");
		HashMap<String, Point> pointmap=getNodes(ins);
		
		Point a=pointmap.get("A");
		Point b=pointmap.get("B");
		Point c=pointmap.get("C");
		Point d=pointmap.get("D");
		Point e=pointmap.get("E");
		
		Point l=pointmap.get("L");
		Point n=pointmap.get("N");
		Point m=pointmap.get("M");
		Point o=pointmap.get("O");
		Point f=pointmap.get("F");
		
		Point g=pointmap.get("G");
		Point h=pointmap.get("H");
		Point i=pointmap.get("I");
		Point j=pointmap.get("J");
		Point k=pointmap.get("K");
		
		for(String s:pointmap.keySet()){
			System.out.println(String.format("id(%s),longitude(%f),latitude(%f)",s, pointmap.get(s).longitude,pointmap.get(s).latitude));
		}
		
		//最顶层区域边界
		Line ab=new Line();ab.add(a);ab.add(b);
		Line bc=new Line();bc.add(b);bc.add(c);
		Line cd=new Line();cd.add(c);cd.add(d);
		Line de=new Line();de.add(d);de.add(e);
		Line ef=new Line();ef.add(e);ef.add(f);
		Line fg=new Line();fg.add(f);fg.add(g);
		Line gh=new Line();gh.add(g);gh.add(h);
		Line hi=new Line();hi.add(h);hi.add(i);
		Line ij=new Line();ij.add(i);ij.add(j);
		Line jk=new Line();jk.add(j);jk.add(k);
		Line kl=new Line();kl.add(k);kl.add(l);
		Line la=new Line();la.add(l);la.add(a);
		
		//中间层次边界
		Line cm=new Line();cm.add(c);cm.add(m);
		Line mi=new Line();mi.add(m);mi.add(i);
		
		
		//最底层边界
		Line bn=new Line();bn.add(b);bn.add(n);
		Line nj=new Line();nj.add(n);nj.add(j);
		Line do_=new Line();do_.add(d);do_.add(o);
		Line oh=new Line();oh.add(o);oh.add(h);
		Line ln=new Line();ln.add(l);ln.add(n);
		Line nm=new Line();nm.add(n);nm.add(m);
		Line mo=new Line();mo.add(m);mo.add(o);
		Line of=new Line();of.add(o);of.add(f);
		
		//生成最外层区域的边界
		Boundary layer1=new Boundary();
		layer1.add(ab);layer1.add(bc);layer1.add(cd);layer1.add(de);
		layer1.add(ef);layer1.add(fg);
		layer1.add(gh);layer1.add(hi);layer1.add(ij);layer1.add(jk);
		layer1.add(kl);layer1.add(la);
		
		//生成中间层区域的边界,左边区域
		Boundary layer1_1=new Boundary();
		layer1_1.add(ab);layer1_1.add(bc);
		layer1_1.add(cm);layer1_1.add(mi);
		layer1_1.add(ij);layer1_1.add(jk);
		layer1_1.add(kl);layer1_1.add(la);
		//生成中间层区域的边界,右边区域
		Boundary layer1_2=new Boundary();
		layer1_2.add(cd);layer1_2.add(de);
		layer1_2.add(ef);layer1_2.add(fg);
		layer1_2.add(gh);layer1_2.add(hi);
		layer1_2.add(mi);layer1_2.add(cm);
		
		//生成最底层的区域的边界,一共八个
		Boundary layer1_1_1=new Boundary();
		layer1_1_1.add(ab);
		layer1_1_1.add(bn);
		layer1_1_1.add(ln);
		layer1_1_1.add(la);
		Boundary layer1_1_2=new Boundary();
		layer1_1_2.add(bc);
		layer1_1_2.add(cm);
		layer1_1_2.add(nm);
		layer1_1_2.add(bn);
		Boundary layer1_1_3=new Boundary();
		layer1_1_3.add(ln);
		layer1_1_3.add(nj);
		layer1_1_3.add(jk);
		layer1_1_3.add(kl);
		Boundary layer1_1_4=new Boundary();
		layer1_1_4.add(nm);
		layer1_1_4.add(mi);
		layer1_1_4.add(ij);
		layer1_1_4.add(nj);
		
		Boundary layer1_2_1=new Boundary();
		layer1_2_1.add(cd);
		layer1_2_1.add(do_);
		layer1_2_1.add(mo);
		layer1_2_1.add(cm);
		Boundary layer1_2_2=new Boundary();
		layer1_2_2.add(de);
		layer1_2_2.add(ef);
		layer1_2_2.add(of);
		layer1_2_2.add(do_);
		Boundary layer1_2_3=new Boundary();
		layer1_2_3.add(mo);
		layer1_2_3.add(oh);
		layer1_2_3.add(hi);
		layer1_2_3.add(mi);
		Boundary layer1_2_4=new Boundary();
		layer1_2_4.add(of);
		layer1_2_4.add(fg);
		layer1_2_4.add(gh);
		layer1_2_4.add(oh);
		
		//生成测试的层次树
		AreaTree.getInstance().root=new AreaNode();
		AreaTree.getInstance().root.addBoundary(layer1);
		
		AreaNode midlay1_1=new AreaNode();
		midlay1_1.addBoundary(layer1_1);
		AreaNode midlay1_2=new AreaNode();
		midlay1_2.addBoundary(layer1_2);
		
		AreaNode underlay1_1_1=new AreaNode();
		underlay1_1_1.addBoundary(layer1_1_1);
		AreaNode underlay1_1_2=new AreaNode();
		underlay1_1_2.addBoundary(layer1_1_2);
		AreaNode underlay1_1_3=new AreaNode();
		underlay1_1_3.addBoundary(layer1_1_3);
		AreaNode underlay1_1_4=new AreaNode();
		underlay1_1_4.addBoundary(layer1_1_4);
		
		AreaNode underlay1_2_1=new AreaNode();
		underlay1_2_1.addBoundary(layer1_2_1);
		AreaNode underlay1_2_2=new AreaNode();
		underlay1_2_2.addBoundary(layer1_2_2);
		AreaNode underlay1_2_3=new AreaNode();
		underlay1_2_3.addBoundary(layer1_2_3);
		AreaNode underlay1_2_4=new AreaNode();
		underlay1_2_4.addBoundary(layer1_2_4);
		
		AreaTree.getInstance().root.addChild(midlay1_1);
		midlay1_1.addChild(underlay1_1_1);
		midlay1_1.addChild(underlay1_1_2);
		midlay1_1.addChild(underlay1_1_3);
		midlay1_1.addChild(underlay1_1_4);
		
		AreaTree.getInstance().root.addChild(midlay1_2);
		midlay1_2.addChild(underlay1_2_1);
		midlay1_2.addChild(underlay1_2_2);
		midlay1_2.addChild(underlay1_2_3);
		midlay1_2.addChild(underlay1_2_4);
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
	
	public static HashMap<String,Point>  getNodes(InputStream ins) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//      System.out.println("class name: " + dbf.getClass().getName());
		// step 2:获得具体的dom解析器
		DocumentBuilder db = dbf.newDocumentBuilder();
		//      System.out.println("class name: " + db.getClass().getName());
		// step3: 解析一个xml文档，获得Document对象（根结点）
		Document document = db.parse(ins);
		NodeList list = document.getElementsByTagName("nodes");
		
		HashMap<String,Point> pointmap=new HashMap<String,Point>();
		
		for(int i=0;i<list.getLength();i++){
			Node node=list.item(i);
			if(node!=null && node.getNodeType()==Node.ELEMENT_NODE ){
//				System.out.println("读取子节点"+node.getNodeName());
				NodeList list2=((Element)node).getChildNodes();
//				System.out.println(list2.getLength());
				for(int j=0;j<list2.getLength();j++){
					Node node2=list2.item(j);
//					System.out.println("nodename:"+node2.getNodeName());
//					System.out.println("value:"+node2.getNodeValue());
					if(node2.getNodeName().equals("node") && node2.hasAttributes()){
						Node temp=node2.getAttributes().getNamedItem("id");
						String id=temp.getNodeValue();
						temp=node2.getAttributes().getNamedItem("x");
//						System.out.println(temp.getNodeValue());
						String str_x=temp.getNodeValue();
						temp=node2.getAttributes().getNamedItem("y");
//						System.out.println(temp.getNodeValue());
						String str_y=temp.getNodeValue();
						
						pointmap.put(id, new Point(Double.valueOf(str_x), Double.valueOf(str_y)));
					}
				}
			}
			else{
				System.out.println("node is null or node don't has childNodes");
			}
		}
		
		return pointmap;
	}
}
