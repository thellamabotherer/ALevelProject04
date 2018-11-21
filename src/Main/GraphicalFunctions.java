package Main;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Data.Matrix2d;

public class GraphicalFunctions {

	public static void drawArrow(Window window, Vector2f start, Vector2f direction, float scale) {

		if (direction != null) {
			
			Matrix2d lineBuffer;
			
			double angle = (float)Math.atan((direction.y)/(direction.x));
			float[][]  t = { { (float) Math.cos(angle) , (float)Math.sin(angle) } , { (float)-Math.sin(angle) , (float)Math.cos(angle) } };
			Matrix2d transform = new Matrix2d(t);
			float [][]  line1 = { {-1 , -0.2f} , {1, (float) -0.2} };
			float [][] line2 = { {1, -0.2f} , {-1, -0.2f} } ;
			
			window.changeColour(ColourPalette.red);
			
			window.beginRender();
			lineBuffer = new Matrix2d (line1) ;
			lineBuffer = lineBuffer.mult(transform);
			window.addVertex(new Vector3f ( start.x + scale * lineBuffer.getMat()[0][0] , start.y + scale * lineBuffer.getMat()[1][0] , 0));
			window.addVertex(new Vector3f ( start.x + scale * lineBuffer.getMat()[0][1] , start.y + scale * lineBuffer.getMat()[1][1] , 0));
			lineBuffer = new Matrix2d (line2);
			lineBuffer = lineBuffer.mult(transform);
			window.addVertex(new Vector3f ( start.x + scale * lineBuffer.getMat()[0][0] , start.y + scale * lineBuffer.getMat()[1][0] , 0));
			window.endRender();
			
			line1[0][0] = 1;
			line1[0][1] = -0.2f;
			line1[1][0] = -1;
			line1[1][1] = 0.2f;
			line2[0][0] = -1;
			line2[0][1] = 0.2f;
			line2[1][0] = 1;
			line2[1][1] = 0.2f;
			
			window.beginRender();
			lineBuffer = new Matrix2d (line1) ;
			lineBuffer = lineBuffer.mult(transform);
			window.addVertex(new Vector3f ( start.x + scale * lineBuffer.getMat()[0][0] , start.y + scale * lineBuffer.getMat()[1][0] , 0));
			window.addVertex(new Vector3f ( start.x + scale * lineBuffer.getMat()[0][1] , start.y + scale * lineBuffer.getMat()[1][1] , 0));
			lineBuffer = new Matrix2d (line2);
			lineBuffer = lineBuffer.mult(transform);
			window.addVertex(new Vector3f ( start.x + scale * lineBuffer.getMat()[0][0] , start.y + scale * lineBuffer.getMat()[1][0] , 0));
			window.endRender();
			
			line1[0][0] = 1;
			line1[0][1] = 1;
			line1[1][0] = 1.5f;
			line1[1][1] = 0;
			line2[0][0] = 1.5f;
			line2[0][1] = 0;
			line2[1][0] = 1;
			line2[1][1] = -1;
			
			window.beginRender();
			lineBuffer = new Matrix2d (line1) ;
			lineBuffer = lineBuffer.mult(transform);
			window.addVertex(new Vector3f ( start.x + scale * lineBuffer.getMat()[0][0] , start.y + scale * lineBuffer.getMat()[1][0] , 0));
			window.addVertex(new Vector3f ( start.x + scale * lineBuffer.getMat()[0][1] , start.y + scale * lineBuffer.getMat()[1][1] , 0));
			lineBuffer = new Matrix2d (line2);
			lineBuffer = lineBuffer.mult(transform);
			window.addVertex(new Vector3f ( start.x + scale * lineBuffer.getMat()[0][0] , start.y + scale * lineBuffer.getMat()[1][0] , 0));
			window.endRender();
			
			
			
		}

	}

}
