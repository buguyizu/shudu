package cn;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
public class Shudu {
    // a 4d integer array of all sudu data
    private static int array4D[][][][] = new int[][][][] {
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
    private static final int RANGE[] = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    // a map contains integer data range of every cell
    private static Map<String, int[]> rangeMap = new HashMap<String, int[]>();
    // a variable temporarily keeping the value of rollback action
    private static int e = 0;
    // a variable of all cell setting action count
    private static int c = 0;
    public static void main(String[] args) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        Logger.getAnonymousLogger().info(df.format(new Date()) + " START \n");
        // cell setting action
        setTableValue();
        // print all array data
        print();
        // pring all cell setting action count
        Logger.getAnonymousLogger().info(Integer.toString(c) + "");
        Logger.getAnonymousLogger().info(df.format(new Date()) + " END \n");
    }
    private static void print() {
        StringBuffer info = new StringBuffer("\n");
        for (int i = 0; i < 3; i++, info.append("\n")) {
            for (int j = 0; j < 3; j++, info.append("\n")) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        // array4D[i][j][k][l] = (i + 1) * 1000 + (j + 1) * 100
                        // + (k + 1) * 10 + l + 1;
                        info.append(array4D[i][j][k][l] + "\t ");
                    }
                }
            }
        }
        Logger.getAnonymousLogger().info(info.toString());
    }
    private static void setTableValue() {
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
    private static int getCellValue(int[] range) {
        if (range == null || range.length == 0) {
            return 0;
        } else {
            int l = 0, u = range.length - 1;
            int rnd = new Double((u - l + 1) * Math.random() + l).intValue();
            return range[rnd];
        }
    }
    private static void setCellRange(int i, int j, int k, int l) {
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
    private static String getMapKey(int i, int j, int k, int l) {
        int v = i * 1000 + j * 100 + k * 10 + l * 1;
        return StringUtils.leftPad(Integer.toString(v), 4, '0');
    }
}