package Main;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class GraphicalFunctions {

	public static void drawArrow(Window window, Vector2f start, Vector2f direction) {

		if (direction != null) {
			window.beginLineRender();
			window.changeColour(ColourPalette.red);
			window.addVertex(new Vector3f(start.x - direction.x / 2, start.y - direction.y / 2, 0));
			window.addVertex(new Vector3f(start.x + direction.x / 2, start.y + direction.y / 2, 0));
			window.addVertex(new Vector3f(start.x - direction.x / 2, start.y - direction.y / 2, 0));
			window.endRender();
			window.beginRender();
			window.addVertex(new Vector3f((float) (start.x + (direction.x / 2) * 1.5),
					(float) (start.y + (direction.y / 2) * 1.5), 0));
			window.addVertex(new Vector3f((float) (start.x + (direction.x / 2) * 0.5),
					(float) (start.y + (direction.y / 2) * 1.5), 0));
			window.addVertex(new Vector3f((float) (start.x + (direction.x / 2) * 0.5),
					(float) (start.y + (direction.y / 2) * 0.5), 0));
			window.endRender();
			window.beginRender();
			window.addVertex(new Vector3f((float) (start.x + (direction.x / 2) * 1.5),
					(float) (start.y + (direction.y / 2) * 1.5), 0));
			window.addVertex(new Vector3f((float) (start.x + (direction.x / 2) * 1.5),
					(float) (start.y + (direction.y / 2) * 0.5), 0));
			window.addVertex(new Vector3f((float) (start.x + (direction.x / 2) * 0.5),
					(float) (start.y + (direction.y / 2) * 0.5), 0));
			window.endRender();
		}

	}

}
