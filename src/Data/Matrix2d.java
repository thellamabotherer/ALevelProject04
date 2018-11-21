package Data;

public class Matrix2d<T> {
	
	private T[][] mat;
	
	public Matrix2d (T[][] Mat) {
		this.mat = Mat;
	}
	
	@SuppressWarnings("unchecked")
	public Matrix2d<T> mult (Matrix2d<T> transform) {
		Object[][] image = new Object[transform.rows()][this.columns()];
		for (int r = 0; r < image.length; r++) {
			for (int c = 0; c < image[0].length; c++) {
				if (r == 0) {
					image[r][c] = (Float)this.mat[0][c] * (Float)transform.getMat()[0][0] 
							+ (Float)this.mat[1][c] * (Float)transform.getMat()[0][1];
				}else if (r == 1) {
					image[r][c] = (Float)this.mat[0][c] * (Float)transform.getMat()[1][0] 
							+ (Float)this.mat[1][c] * (Float)transform.getMat()[1][1];
				}
			}
		}
		return new Matrix2d (image);
	}
	
	public void print () {
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				System.out.print(mat[i][j] + " ");
			}System.out.print("\n");
		}
	}
	
	public int rows () {
		return this.mat.length;
	}public int columns () {
		return this.mat[0].length;
	}public T[][] getMat () {
		return this.mat;
	}
	
}
