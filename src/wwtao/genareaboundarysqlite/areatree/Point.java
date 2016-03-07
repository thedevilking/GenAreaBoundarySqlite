package wwtao.genareaboundarysqlite.areatree;

/**
 * 
 * @author wwtao
 * 说明：经纬度坐标
 * 
 */
public class Point {

	public int id=AreaTree.genId();
	public double longitude;
	public double latitude;
	
	public Point(double longitude,double latitude){
		this.id=AreaTree.genId();
		this.longitude=longitude;
		this.latitude=latitude;
	}
}
