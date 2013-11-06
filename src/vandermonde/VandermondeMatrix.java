package vandermonde;

import galoisfield.GFMatrix;
import galoisfield.GalloisField2;

public class VandermondeMatrix extends GFMatrix {
	private static final long serialVersionUID = 651250354994493088L;

	public VandermondeMatrix(int m, int n, GalloisField2 gf) {
		super(gf, m, m);
		if(m<n){
			throw new RuntimeException("M must be < N! m="+m+" n="+n);
		}

		for(int i=0; i<m; i++){
			for(int j=0; j<n; j++){
				this.set(i, j, gf.pow(j+1, i));
			}
		}
	}
	


}
