package Data;

public class Parabola {
	
	public static boolean focus = true;
	public static boolean vertex = false;
	
	private boolean type;
	private Point point;
	private Edge edge;
	private Event event;
	
	public void describe () {
		System.out.println("type = " + this.type + ", true = focus and false = vertex");
		System.out.println("point = " + this.point);
		System.out.println("Edge = " + this.edge + " edge follows");
		this.edge.describe();
		System.out.println("Event = " + this.event);
	}
	
	public Edge getEdge() {
		return edge;
	}
	public void setEdge(Edge edge) {
		this.edge = edge;
	}
	private Parabola parent;
	Parabola leftChild;
	Parabola rightChild;
	
	public Parabola () {
		this.type = Parabola.vertex;
	}public Parabola (Point p){
		this.point = p;
		this.type = Parabola.focus;
	}
	
	public void setLeftChild (Parabola p) {
		this.leftChild = p;
		p.setParent(this);
	}public void setRightChild (Parabola p) {
		this.rightChild = p;
		p.setParent(this);
	}
	
	public static Parabola getLeft (Parabola p) {
		return getLeftChild(getLeftParent(p));
	}public static Parabola getRight (Parabola p) {
		return getRightChild(getRightParent(p));
	}
	
	public static Parabola getLeftParent (Parabola p) {
		Parabola parent = p.getParent();
		if (parent == null) {
			return null;
		}
		
		Parabola last = p;
		while (parent.leftChild == null) {
			if (parent.parent == null) {
				return null;
			}last = parent;
			parent = parent.parent;
		}
		return parent;
	}public static Parabola getRightParent (Parabola p) {                 // why so many parents, parent no longer looks like a real word
		Parabola parent = p.getParent();
		if (parent == null) {
			return null;
		}
		
		Parabola last = p;
		while (parent.leftChild == null) {                                // parent.parent.parent.parent.parent.parent.parent.parent.parent.parent.parent.parent.parent.parent.aargh
			if (parent.parent == null) {
				return null;
			}last = parent;
			parent = parent.parent;
		}
		return parent;
	}
	
	public static Parabola getLeftChild (Parabola p) {
		if (p == null) {
			return null;
		}Parabola child = p.leftChild;
		while (!child.type) {
			child = child.rightChild;
		}return child;
	}
	public static Parabola getRightChild (Parabola p) {
		if (p == null) {
			return null;
		}Parabola child = p.rightChild;
		while (!child.type) {
			child = child.leftChild;
		}return child;
	}
	
	public boolean getType() {
		return this.type;
	}public void setType (boolean t) {
		this.type = t;
	}public Parabola getParent() {
		return this.parent;
	}public void setParent (Parabola p) {
		this.parent = p;
	}
	public static boolean isFocus() {
		return focus;
	}
	public static void setFocus(boolean focus) {
		Parabola.focus = focus;
	}
	public static boolean isVertex() {
		return vertex;
	}
	public static void setVertex(boolean vertex) {
		Parabola.vertex = vertex;
	}
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public Parabola getLeftChild() {
		return leftChild;
	}
	public Parabola getRightChild() {
		return rightChild;
	}

}
