package Data;

public class Parabola {
	
	public static boolean focus = true;
	public static boolean vertex = false;
	
	private boolean type;
	private Point point;
	private Edge edge;
	private Event event;
	
	public void describe () {
		String lC;
		String rC;
		if (this.leftChild != null) {lC = "YES";}
		else {lC = "NO ";};
		if (this.rightChild != null) {rC = "YES";}
		else {rC = "NO ";};
		System.out.println("Left " + lC + "| Right " + rC);
	}
	
	public Edge getEdge() {
		return edge;
	}
	public void setEdge(Edge edge) {
		this.edge = edge;
	}
	private Parabola parent;
	
	private Parabola leftChild;
	private Parabola rightChild;
	
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
	
	// returns the closest left site (focus of parabola) 
		public static Parabola getLeft(Parabola p) {
			return getLeftChild(getLeftParent(p));
		}
		
		// returns closest right site (focus of parabola)
		public static Parabola getRight(Parabola p) {
			return getRightChild(getRightParent(p));
		}
		
		// returns the closest parent on the left
		public static Parabola getLeftParent(Parabola p) {
			Parabola parent = p.parent;
			if (parent == null) return null;
			Parabola last = p;
			while (parent.getLeftChild() == last) {
				if(parent.parent == null) return null;
				last = parent;
				parent = parent.parent;
			}
			return parent;
		}
		
		// returns the closest parent on the right
		public static Parabola getRightParent(Parabola p) {
			Parabola parent = p.parent;
			if (parent == null) return null;
			Parabola last = p;
			while (parent.getRightChild() == last) {
				if(parent.parent == null) return null;
				last = parent;
				parent = parent.parent;
			}
			return parent;
		}
		
		// returns closest site (focus of another parabola) to the left
		public static Parabola getLeftChild(Parabola p) {
			if (p == null) return null;
			Parabola child = p.getLeftChild();
			while(child.type == Parabola.vertex) child = child.getRightChild();
			return child;
		}
		
		// returns closest site (focus of another parabola) to the right
		public static Parabola getRightChild(Parabola p) {
			if (p == null) return null;
			Parabola child = p.getRightChild();
			while(child.type == Parabola.vertex) child = child.getLeftChild();
			return child;	
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
	
	public void leafCheck () {
		if (this.leftChild == null && this.rightChild == null) {
			this.type = this.focus;
		}
	}

}
