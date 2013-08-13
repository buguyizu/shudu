package cn;

public class jiuGong {

	public static void main(String[] args) {

		int n = 9;

		int g[][] = new int[n][n];

		int max = n * n;

		g[0][(n - 1) / 2] = 1;

		int x = 0;
		int y = (n - 1) / 2;

		for (int i = 2; i <= max; i++) {

			if (x == 0 && y != n - 1) {

				x = n - 1;
				y = y + 1;
				g[x][y] = i;

			} else if (y == n - 1 && x != 0) {

				y = 0;
				x = x - 1;
				g[x][y] = i;

			} else {

				if ((x - 1 < 0 && y + 1 == n) || (g[x - 1][y + 1] != 0)) {

					x = x + 1;
					g[x][y] = i;

				} else {

					x = x - 1;
					y = y + 1;
					g[x][y] = i;

				}
			}
		}

		// print
		for (int i = 0; i <= n - 1; i++) {
			for (int j = 0; j <= n - 1; j++) {
				System.out.print(g[i][j]);
				System.out.print("\t");
			}
			System.out.println("");
		}
	}

}
