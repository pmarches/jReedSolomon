package galoisfield;

   /** LU Decomposition.
   <P>
   For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n
   unit lower triangular matrix L, an n-by-n upper triangular matrix U,
   and a permutation vector piv of length m so that A(piv,:) = L*U.
   If m < n, then L is m-by-m and U is m-by-n.
   <P>
   The LU decompostion with pivoting always exists, even if the matrix is
   singular, so the constructor will never fail.  The primary use of the
   LU decomposition is in the solution of square systems of simultaneous
   linear equations.  This will fail if isNonsingular() returns false.
   */

public class GFLUDecomposition {
   /** Array for internal storage of decomposition.
   @serial internal array storage.
   */
   private int[][] LU;

   /** Row and column dimensions, and pivot sign.
   @serial column dimension.
   @serial row dimension.
   @serial pivot sign.
   */
   private int m, n, pivsign; 

   /** Internal storage of pivot vector.
   @serial pivot vector.
   */
   private int[] piv;

   private GalloisField2 gf;

/* ------------------------
   Constructor
 * ------------------------ */

   /** LU Decomposition
   @param  A   Rectangular matrix
   @return     Structure to access L, U and piv.
   */

   public GFLUDecomposition (GFMatrix A, GalloisField2 gf) {
	   this.gf = gf;
   // Use a "left-looking", dot-product, Crout/Doolittle algorithm.

      LU = A.getArrayCopy();
      m = A.rowCount();
      n = A.columnCount();
      piv = new int[m];
      for (int i = 0; i < m; i++) {
         piv[i] = i;
      }
      pivsign = 1;
      int[] LUrowi;
      int[] LUcolj = new int[m];

      // Outer loop.

      for (int j = 0; j < n; j++) {

         // Make a copy of the j-th column to localize references.

         for (int i = 0; i < m; i++) {
            LUcolj[i] = LU[i][j];
         }

         // Apply previous transformations.

         for (int i = 0; i < m; i++) {
            LUrowi = LU[i];

            // Most of the time is spent in the following dot product.

            int kmax = Math.min(i,j);
            int s = 0;
            for (int k = 0; k < kmax; k++) {
               s = gf.add(s, gf.multiply(LUrowi[k], LUcolj[k]));
            }

            LUcolj[i] = gf.sub(LUcolj[i], s);
            LUrowi[j] = LUcolj[i];
         }
   
         // Find pivot and exchange if necessary.

         int p = j;
         for (int i = j+1; i < m; i++) {
            if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p])) {
               p = i;
            }
         }
         if (p != j) {
            for (int k = 0; k < n; k++) {
            	int t = LU[p][k]; LU[p][k] = LU[j][k]; LU[j][k] = t;
            }
            int k = piv[p]; piv[p] = piv[j]; piv[j] = k;
            pivsign = -pivsign;
         }

         // Compute multipliers.
         
         if (j < m & LU[j][j] != 0) {
            for (int i = j+1; i < m; i++) {
               LU[i][j] = gf.divide(LU[i][j], LU[j][j]);
            }
         }
      }
   }

/* ------------------------
   Public Methods
 * ------------------------ */

   /** Is the matrix nonsingular?
   @return     true if U, and hence A, is nonsingular.
   */

   public boolean isNonsingular () {
      for (int j = 0; j < n; j++) {
         if (LU[j][j] == 0)
            return false;
      }
      return true;
   }

   /** Return lower triangular factor
   @return     L
   */

   public GFMatrix getL () {
      GFMatrix X = new GFMatrix(this.gf, m,n);
      int[][] L = X.getArray();
      for (int i = 0; i < m; i++) {
         for (int j = 0; j < n; j++) {
            if (i > j) {
               L[i][j] = LU[i][j];
            } else if (i == j) {
               L[i][j] = 1;
            } else {
               L[i][j] = 0;
            }
         }
      }
      return X;
   }

   /** Return upper triangular factor
   @return     U
   */

   public GFMatrix getU () {
      GFMatrix X = new GFMatrix(this.gf, n,n);
      int[][] U = X.getArray();
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            if (i <= j) {
               U[i][j] = LU[i][j];
            } else {
               U[i][j] = 0;
            }
         }
      }
      return X;
   }

   /** Return pivot permutation vector
   @return     piv
   */

   public int[] getPivot () {
      int[] p = new int[m];
      for (int i = 0; i < m; i++) {
         p[i] = piv[i];
      }
      return p;
   }

   /** Return pivot permutation vector as a one-dimensional double array
   @return     (double) piv
   */

   public double[] getDoublePivot () {
      double[] vals = new double[m];
      for (int i = 0; i < m; i++) {
         vals[i] = (double) piv[i];
      }
      return vals;
   }

   /** Determinant
   @return     det(A)
   @exception  IllegalArgumentException  GFMatrix must be square
   */

   public int det () {
      if (m != n) {
         throw new IllegalArgumentException("GFMatrix must be square.");
      }
      int d = pivsign;
      for (int j = 0; j < n; j++) {
         d = gf.multiply(d, LU[j][j]);
      }
      return d;
   }

   /** Solve A*X = B
   @param  B   A GFMatrix with as many rows as A and any number of columns.
   @return     X so that L*U*X = B(piv,:)
   @exception  IllegalArgumentException GFMatrix row dimensions must agree.
   @exception  RuntimeException  GFMatrix is singular.
   */

   public GFMatrix solve (GFMatrix B) {
      if (B.rowCount() != m) {
         throw new IllegalArgumentException("GFMatrix row dimensions must agree.");
      }
      if (!this.isNonsingular()) {
    	  System.out.println("Matrix is singular ?? Trying anyways..");
//         throw new RuntimeException("GFMatrix is singular.");
      }

      // Copy right hand side with pivoting
      int nx = B.columnCount();
      GFMatrix Xmat = B.getSubMatrix(piv,0,nx-1);
      int[][] X = Xmat.getArray();

      // Solve L*Y = B(piv,:)
      for (int k = 0; k < n; k++) {
         for (int i = k+1; i < n; i++) {
            for (int j = 0; j < nx; j++) {
               X[i][j] = gf.sub(X[i][j], gf.multiply(X[k][j], LU[i][k]));
            }
         }
      }
      // Solve U*X = Y;
      for (int k = n-1; k >= 0; k--) {
         for (int j = 0; j < nx; j++) {
            X[k][j] = gf.divide(X[k][j], LU[k][k]);
         }
         for (int i = 0; i < k; i++) {
            for (int j = 0; j < nx; j++) {
               X[i][j] = gf.sub(X[i][j], gf.multiply(X[k][j], LU[i][k]));
            }
         }
      }
      return Xmat;
   }
}
