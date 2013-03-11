package com.appspot.shudu99.shudu;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
public class ShuduServlet extends HttpServlet {

	// a 4d integer array of all shudu data
	private int array4D[][][][] = new int[][][][] {
			{ { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } },
					{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } },
					{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } } },
			{ { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } },
					{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } },
					{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } } },
			{ { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } },
					{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } },
					{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } } } };

	// a constant integer array for random filling cell
	private final int RANGE[] = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	// a map contains integer data range of every cell
	private Map<String, int[]> rangeMap = new HashMap<String, int[]>();

	// a variable temporarily keeping the value of rollback action
	private int e = 0;

	// a variable of all cell setting action count
	private int c = 0;

	// 0000 0001 0002 0010 0011 0012 0020 0021 0022
	// 0100 0101 0102 0110 0111 0112 0120 0121 0122
	// 0200 0201 0202 0210 0211 0212 0220 0221 0222
	//
	// 1000 1001 1002 1010 1011 1012 1020 1021 1022
	// 1100 1101 1102 1110 1111 1112 1120 1121 1122
	// 1200 1201 1202 1210 1211 1212 1220 1221 1222
	//
	// 2000 2001 2002 2010 2011 2012 2020 2021 2022
	// 2100 2101 2102 2110 2111 2112 2120 2121 2122
	// 2200 2201 2202 2210 2211 2212 2220 2221 2222
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");

		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		Logger.getAnonymousLogger().info(df.format(new Date()) + " START \n");

		setTableValue();
		String printString = getPrintString();
		printString = StringUtils.replace(printString, "\t\n", "\n");
		
		resp.getWriter().println("A page to get new sudoku array!");
		resp.getWriter().print(printString);

		Logger.getAnonymousLogger().info(printString + "\n");
		Logger.getAnonymousLogger().info(Integer.toString(c) + "\n");
		Logger.getAnonymousLogger().info(df.format(new Date()) + " END \n");
	}
	
	private String getPrintString() {
		StringBuffer info = new StringBuffer("\n");
		
		for (int i = 0; i < 3; i++, info.append("\n")) {
			for (int j = 0; j < 3; j++, info.append("\n")) {
				for (int k = 0; k < 3; k++) {
					for (int l = 0; l < 3; l++) {
						// array4D[i][j][k][l] = (i + 1) * 1000 + (j + 1) * 100
						// + (k + 1) * 10 + l + 1;
						info.append(array4D[i][j][k][l] + "\t");
					}
				}
			}
		}
		return info.toString();
	}

	
	private void setTableValue() {

		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;

		// a flag for rollback action
		String f = "0";

		for (i = 0; i < 3; i++) {

			if (f == "0") {
				j = 0;
			} else {
				f = "k";
			}
			J: for (j = 0; j < 3; j++) {

				if (f == "0") {
					k = 0;
				} else {
					f = "l";
				}

				K: for (; k < 3; k++) {

					if (f == "0") {
						l = 0;
					} else {
						f = "0";
					}
					L: for (; l < 3; l++) {

						setCellRange(i, j, k, l);
						array4D[i][j][k][l] = getCellValue(rangeMap
								.get(getMapKey(i, j, k, l)));
						c++;

						// when get a invalid value, goto rollback action
						if (array4D[i][j][k][l] == 0) {
							if (l > 0) {
								rangeMap.remove(getMapKey(i, j, k, l));
								e = array4D[i][j][k][l - 1];
								array4D[i][j][k][l - 1] = 0;

								l -= 2;
							} else if (k > 0) {
								rangeMap.remove(getMapKey(i, j, k, l));

								l = 2;
								e = array4D[i][j][k - 1][l];
								array4D[i][j][k - 1][l] = 0;

								k -= 2;
								f = "l";
								// print();
								break L;
							} else if (j > 0) {
								rangeMap.remove(getMapKey(i, j, k, l));

								l = 2;
								k = 2;
								e = array4D[i][j - 1][k][l];
								array4D[i][j - 1][k][l] = 0;

								j -= 2;
								f = "k";
								// print();
								break K;
							} else if (i > 0) {
								rangeMap.remove(getMapKey(i, j, k, l));

								l = 2;
								k = 2;
								j = 2;
								e = array4D[i - 1][j][k][l];
								array4D[i - 1][j][k][l] = 0;

								i -= 2;
								f = "j";
								// print();
								break J;
							}
						}

						// print();
					}
				}
			}
		}
	}

	private int getCellValue(int[] range) {
		if (range == null || range.length == 0) {
			return 0;
		} else {
			int l = 0, u = range.length - 1;
			int rnd = new Double((u - l + 1) * Math.random() + l).intValue();
			return range[rnd];
		}
	}
	
	private void setCellRange(int i, int j, int k, int l) {

		if (rangeMap.containsKey(getMapKey(i, j, k, l))) {

			if (e == 0) {
				return;
			}

			int[] range = rangeMap.get(getMapKey(i, j, k, l));

			// when rollback, remove impossible data
			range = ArrayUtils.removeElement(range, e);
			e = 0;

			rangeMap.put(getMapKey(i, j, k, l), range);

			return;
		}

		if (i == 0 && j == 0 && k == 0 && l == 0) {

			rangeMap.put("0000", RANGE);

		} else {

			int[] range = null;
			range = ArrayUtils.addAll(range, RANGE);

			// while checking, remove repeated data
			// gong only check
			// maybe have a better way
			for (int j1 = 0; j1 < 3; j1++) {
				for (int l1 = 0; l1 < 3; l1++) {
					range = ArrayUtils.removeElement(range,
							array4D[i][j1][k][l1]);
				}
			}

			// hang only check
			for (int k1 = 0; k1 < k; k1++) {
				for (int l1 = 0; l1 < 3; l1++) {
					range = ArrayUtils.removeElement(range,
							array4D[i][j][k1][l1]);
				}
			}

			// lie only check
			for (int i1 = 0; i1 < i; i1++) {
				for (int j1 = 0; j1 < 3; j1++) {
					range = ArrayUtils.removeElement(range,
							array4D[i1][j1][k][l]);
				}
			}

			rangeMap.put(getMapKey(i, j, k, l), range);
		}
	}

	// get the key of each cell for rangeMap
	private String getMapKey(int i, int j, int k, int l) {
		int v = i * 1000 + j * 100 + k * 10 + l * 1;
		return StringUtils.leftPad(Integer.toString(v), 4, '0');
	}
}
