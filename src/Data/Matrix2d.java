package Data;

public class Matrix2d {
	
	private float[][] mat;
	
	/*public <T> Matrix2d (T[][] Mat) {
		this.mat = Mat;
	}*/public Matrix2d (float[][] Mat){
		this.mat = Mat;
	}

	public Matrix2d mult (Matrix2d transform) {
		if (this.xSize() == transform.ySize()) {
			float[][] firstMatrix = transform.getMat();
	        float[][] secondMatrix = this.mat;
	        
	        // Mutliplying Two matrices
	        float[][] image = new float[transform.ySize()][this.xSize()];
	        for(int i = 0; i < transform.ySize(); i++) {
	            for (int j = 0; j < this.xSize(); j++) {
	                for (int k = 0; k < transform.xSize(); k++) {
	                    image[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
	                }
	            }
	        }
			return new Matrix2d (image); 
		}else {
			return null;
		}
	}
	
	public void print () {
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				System.out.print(mat[i][j] + " ");
			}System.out.print("\n");
		}
	}
	
	public int ySize () {
		return this.mat.length;
	}public int xSize () {
		return this.mat[0].length;
	}public float[][] getMat () {
		return this.mat;
	}
	
}
